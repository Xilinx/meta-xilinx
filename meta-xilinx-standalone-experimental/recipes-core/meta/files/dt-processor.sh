#! /bin/bash

# Copyright (c) 2021 Xilinx Inc
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

# This script configures the Yocto Project build system for use with the System
# Device Tree workflow when building for a Xilinx FPGA, such as the ZynqMP or
# Versal.

error() { echo "ERROR: $1" >&2; exit 1; }

warn() { echo "WARNING: $1"; }

info() { echo "INFO: $1"; }

usage() {
  cat <<EOF
$0
    -c <config_dir>         Location of the build conf directory
    -s <system_dtb>         Path to system DTB
    -d <domain_file>        Path to domain file (.yml/.dts)
    [-o <overlay_dtb>]      Generate overlay dts
    [-e <external_fpga>]    Apply a partial overlay
    [-m <machine_conf>]     The name of the machine .conf to generate
    [-t <machine>]          Machine type: zynqmp or versal (usually auto detected)
    [-p <psu_init_path>]    Path to psu_init files, defaults to system_dtb path
    [-i <pdu_path>]         Path to the pdi file
    [-l <config_file>]      write local.conf changes to this file
    [-P <petalinux_schema>] Path to petalinux schema file

EOF
  exit
}

parse_args() {
  [ $# -eq 0 ] && usage

  while getopts ":c:s:d:o:e:m:l:hP:p:i:" opt; do
    case ${opt} in
      c) config_dir=$OPTARG ;;
      s) system_dtb=$OPTARG ;;
      o) overlay_dtb=$OPTARG ;;
      d) domain_file=$OPTARG ;;
      e) external_fpga=$OPTARG ;;
      m) mach_conf=$OPTARG ; mach_conf=${mach_conf%%.conf} ;;
      t) machine=$OPTARG ;;
      p) psu_init_path=$OPTARG ;;
      i) pdi_path=$OPTARG ;;
      l) localconf=$OPTARG ;;
      P) petalinux_schema=$OPTARG ;;
      h) usage ;;
      :) error "Missing argument for -$OPTARG" ;;
      \?) error "Invalid option -$OPTARG" ;;
    esac
  done

  [ -f "${config_dir}/local.conf" ] || error "Invalid config dir: ${config_dir}"
  [ -f "${system_dtb}" ] || error "Unable to find: ${system_dtb}"
  system_dtb=$(realpath ${system_dtb})
  if [ -z "$psu_init_path" ]; then
    psu_init_path=$(dirname ${system_dtb})
  else
    psu_init_path=$(realpath ${psu_init_path})
  fi
  if [ -z "$pdi_path" ]; then
    pdi_path=$(dirname ${system_dtb})
  else
    pdi_path=$(realpath ${pdi_path})
  fi
  if [ -n "$domain_file" ]; then
    domain_file=$(realpath ${domain_file})
  fi

}

detect_machine() {
  if [ -z "${machine}" ]; then
    # Identify the system type first using PSM/PMC/PMU
    while read -r cpu core domain cpu_name os_hint; do
      case ${cpu} in
        pmu-microblaze)
          machine="zynqmp" ;;
        pmc-microblaze | psm-microblaze)
          machine="versal" ;;
      esac
    done <${cpulist}
  fi

  # Machine not provided and we cannot identify..
  [ -z ${machine} ] && \
    error "Unable to autodetect machine type, use -t to specify the machine."

  case ${machine} in
    zynqmp | versal) : ;;
    *) error "Invalid machine type ${machine}; please choose zynqmp or versal"
  esac
}

dump_cpus() {
    prefix="$1"
    while read -r cpu core domain cpu_name os_hint; do
      case ${cpu} in
        \#*) ;;
        \[*) ;;
        pmu-microblaze)   echo "${prefix}zynqmp-pmu ${cpu_name}" ;;
        pmc-microblaze)   echo "${prefix}versal-plm ${cpu_name}" ;;
        psm-microblaze)   echo "${prefix}versal-psm ${cpu_name}" ;;
        xlnx,microblaze)  echo "${prefix}microblaze ${core} ${cpu_name}";;
        arm,*)            echo "${prefix}${cpu/,/ } ${core} ${cpu_name}";;
        *)                echo "${prefix}${cpu} ${core} ${cpu_name}";;
      esac
    done <${cpulist}
}

cortex_a53_linux() {
  info "cortex-a53 for Linux [ $1 ]"

  if [ "$1" = "None" ]; then
    dtb_file="cortexa53-${machine}-linux.dtb"
    dts_file="cortexa53-${machine}-linux.dts"
    system_conf=${dtb_file}
    conf_file=cortexa53-${machine}-linux.conf
  else
    dtb_file="cortexa53-${machine}-$1-linux.dtb"
    dts_file="cortexa53-${machine}-$1-linux.dts"
    multiconf="${multiconf} cortexa53-${machine}-linux"
    conf_file=multiconfig/cortexa53-${machine}-$1-linux.conf
  fi

  # Check if it is overlay dts otherwise just create linux dtb
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ "${overlay_dtb}" = "true" ]; then
      if [ "${external_fpga}" = "true" ]; then
        LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- xlnx_overlay_dt ${machine} full \
          || error "lopper failed"
      else
        LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} "${system_dtb}" -- xlnx_overlay_dt ${machine} partial \
          || error "lopper failed"
      fi
      dtc -q -O dtb -o pl.dtbo -b 0 -@ pl.dtsi || error "dtc failed"
    elif [ -n "${domain_file}" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a53-imux.dts" \
        -i "${lops_dir}/lop-domain-linux-a53.dts" \
        -i "${lops_dir}/lop-domain-linux-a53-prune.dts" \
	"${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a53-imux.dts" \
        -i "${lops_dir}/lop-domain-linux-a53.dts" \
        -i "${lops_dir}/lop-domain-linux-a53-prune.dts" \
	"${system_dtb}" "${dts_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a53-imux.dts" \
        -i "${lops_dir}/lop-domain-linux-a53.dts" \
        -i "${lops_dir}/lop-domain-linux-a53-prune.dts" \
        "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a53-imux.dts" \
        -i "${lops_dir}/lop-domain-linux-a53.dts" \
        -i "${lops_dir}/lop-domain-linux-a53-prune.dts" \
        "${system_dtb}" "${dts_file}" \
        || error "lopper failed"
    fi
    rm -f pl.dtsi lop-a53-imux.dts.dtb lop-domain-linux-a53.dts.dtb
  )

  if [ "$1" = "None" ]; then
      return $?
  fi

  ## Generate a multiconfig
  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"
EOF
}

a53_fsbl_done=0
cortex_a53_baremetal() {
  if [ "$1" = "fsbl" ]; then
    [ ${a53_fsbl_done} = 1 ] && return
    info "cortex-a53 FSBL baremetal configuration"
  else
    info "cortex-a53 baremetal configuration for core $2 [ $1 ]"
  fi

  suffix=""; lto="-nolto"
  if [ "$1" != "None" ]; then
    suffix="-$1"; lto=""
  fi

  dtb_file="cortexa53-$2-${machine}${suffix}-baremetal.dtb"
  multiconf="${multiconf} cortexa53-$2-${machine}${suffix}-baremetal"
  conf_file="multiconfig/cortexa53-$2-${machine}${suffix}-baremetal.conf"
  libxil="multiconfig/includes/cortexa53-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexa53-${machine}${suffix}-distro.conf"
  yocto_distro="xilinx-standalone${lto}"
  if [ "$1" = "fsbl" ]; then
    fsbl_mcdepends="mc::${dtb_file%%.dtb}:fsbl-firmware:do_deploy"
    fsbl_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"
    multiconf_min="${multiconf_min} cortexa53-$2-${machine}${suffix}-baremetal"
    a53_fsbl_done=1
  fi

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ -n "${domain_file}" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a53-imux.dts" "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a53-imux.dts" \
        "${system_dtb}" "${dtb_file}" || error "lopper failed"
    fi
    rm -f lop-a53-imux.dts.dtb
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  if [ "$1" = "fsbl" ]; then
    if [ ! -e "${psu_init_path}/psu_init.c" ]; then
      warn "Warning: Unable to find psu_init.c in ${psu_init_path}"
    fi
    if [ ! -e "${psu_init_path}/psu_init.h" ]; then
      warn "Warning: Unable to find psu_init.h in ${psu_init_path}"
    fi

    cat <<EOF >"${conf_file}"
PSU_INIT_PATH = "${psu_init_path}"
EOF
  else
    cat /dev/null >"${conf_file}"
  fi
  cat <<EOF >>"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$3"
DEFAULTTUNE = "cortexa53"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "${yocto_distro}"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_a53_freertos() {
  info "cortex-a53 FreeRTOS configuration for core $2 [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexa53-$2-${machine}${suffix}-freertos.dtb"
  multiconf="${multiconf} cortexa53-$2-${machine}${suffix}-freertos"
  conf_file="multiconfig/cortexa53-$2-${machine}${suffix}-freertos.conf"
  libxil="multiconfig/includes/cortexa53-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexa53-${machine}${suffix}-distro.conf"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ -n "${domain_file}" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a53-imux.dts" "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a53-imux.dts" \
        "${system_dtb}" "${dtb_file}" || error "lopper failed"
    fi
    rm -f lop-a53-imux.dts.dtb
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $3 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$3"
DEFAULTTUNE = "cortexa53"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_a72_linux() {
  info "cortex-a72 for Linux [ $1 ]"

  # Find the first file ending in .pdi
  full_pdi_path=$(ls ${pdi_path}/*.pdi 2>/dev/null | head -n 1)
  if [ -z "${full_pdi_path}" ]; then
    error "Unable to find a pdi file in ${pdi_path}, use the -i option to point to the directory containing a .pdi file"
    full_pdi_path="__PATH TO PDI FILE HERE__"
  elif [ "${full_pdi_path}" != "$(ls ${pdi_path}/*.pdi 2>/dev/null)" ]; then
    warn "Warning: multiple PDI files found, using first found $(basename ${full_pdi_path})."
  fi

  if [ "$1" = "None" ]; then
    dtb_file="cortexa72-${machine}-linux.dtb"
    dts_file="cortexa72-${machine}-linux.dts"
    system_conf=${dtb_file}
    conf_file=cortexa72-${machine}-linux.conf
  else
    dtb_file="cortexa72-${machine}-$1-linux.dtb"
    dts_file="cortexa72-${machine}-$1-linux.dts"
    multiconf="${multiconf} cortexa72-${machine}-linux"
    conf_file=multiconfig/cortexa72-${machine}-$1-linux.conf
  fi

  (
    cd dtb || error "Unable to cd to dtb dir"
    # Check if it is overlay dts otherwise just create linux dtb
    if [ "${overlay_dtb}" = "true" ]; then
      # As there is no partial support on Versal, As per fpga manager implementatin there is
      # a flag "external_fpga" which says apply overlay without loading the bit file.
      if [ "${external_fpga}" = "true" ]; then
        LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- xlnx_overlay_dt \
          ${machine} full external_fpga || error "lopper failed"
      else
        # If there is no external_fpga flag, then the default is full
        LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} "${system_dtb}" -- xlnx_overlay_dt \
          ${machine} full || error "lopper failed"
      fi
      dtc -q -O dtb -o pl.dtbo -b 0 -@ pl.dtsi || error "dtc failed"
    elif [ -n "${domain_file}" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a72-imux.dts" \
        -i "${lops_dir}/lop-domain-a72.dts" \
        -i "${lops_dir}/lop-domain-a72-prune.dts" \
	"${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a72-imux.dts" \
        -i "${lops_dir}/lop-domain-a72.dts" \
        -i "${lops_dir}/lop-domain-a72-prune.dts" \
	"${system_dtb}" "${dts_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a72-imux.dts" \
        -i "${lops_dir}/lop-domain-a72.dts" \
        -i "${lops_dir}/lop-domain-a72-prune.dts" \
	"${system_dtb}" "${dtb_file}" || error "lopper failed"
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a72-imux.dts" \
        -i "${lops_dir}/lop-domain-a72.dts" \
        -i "${lops_dir}/lop-domain-a72-prune.dts" \
	"${system_dtb}" "${dts_file}" || error "lopper failed"
    fi
    rm -f pl.dtsi lop-a72-imux.dts.dtb lop-domain-a72.dts.dtb
  )

  if [ "$1" = "None" ]; then
      return $?
  fi

  ## Generate a multiconfig
  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"
EOF
}

cortex_a72_baremetal() {
  info "cortex-a72 baremetal configuration for core $2 [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexa72-$2-${machine}${suffix}-baremetal.dtb"
  multiconf="${multiconf} cortexa72-$2-${machine}${suffix}-baremetal"
  conf_file="multiconfig/cortexa72-$2-${machine}${suffix}-baremetal.conf"
  libxil="multiconfig/includes/cortexa72-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexa72-${machine}${suffix}-distro.conf"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ -n "${domain_file}" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f   --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a72-imux.dts" "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a72-imux.dts" \
        "${system_dtb}" "${dtb_file}" || error "lopper failed"
    fi
    rm -f lop-a72-imux.dts.dtb
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $3 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$3"
DEFAULTTUNE = "cortexa72"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone-nolto"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_a72_freertos() {
  info "cortex-a72 FreeRTOS configuration for core $2 [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexa72-$2-${machine}${suffix}-freertos.dtb"
  multiconf="${multiconf} cortexa72-$2-${machine}${suffix}-freertos"
  conf_file="multiconfig/cortexa72-$2-${machine}${suffix}-freertos.conf"
  libxil="multiconfig/includes/cortexa72-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexa72-${machine}${suffix}-distro.conf"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ -n "${domain_file}" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" lopper -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-a72-imux.dts" "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-a72-imux.dts" \
        "${system_dtb}" "${dtb_file}" || error "lopper failed"
    fi
    rm -f lop-a72-imux.dts.dtb
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $3 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$3"
DEFAULTTUNE = "cortexa72"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

r5_fsbl_done=0
cortex_r5_baremetal() {
  if [ "$1" = "fsbl" ]; then
    [ ${r5_fsbl_done} = 1 ] && return
    info "cortex-r5 FSBL baremetal configuration"
  else
    info "cortex-r5 baremetal configuration for core $2 [ $1 ]"
  fi

  suffix=""; lto="-nolto"
  if [ "$1" != "None" ]; then
    suffix="-$1"; lto=""
  fi

  dtb_file="cortexr5-$2-${machine}${suffix}-baremetal.dtb"
  multiconf="${multiconf} cortexr5-$2-${machine}${suffix}-baremetal"
  conf_file="multiconfig/cortexr5-$2-${machine}${suffix}-baremetal.conf"
  libxil="multiconfig/includes/cortexr5-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexr5-${machine}${suffix}-distro.conf"
  yocto_distro="xilinx-standalone${lto}"

  if [ "$1" = "fsbl" ]; then
    r5fsbl_mcdepends="mc::${dtb_file%%.dtb}:fsbl-firmware:do_deploy"
    r5fsbl_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"
    r5_fsbl_done=1
  fi

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ -n "$domain_file" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-r5-imux.dts" "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-r5-imux.dts" \
        "${system_dtb}" "${dtb_file}" || error "lopper failed"
    fi
    rm -f lop-r5-imux.dts.dtb
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $3 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  if [ "$1" = "fsbl" ]; then
    if [ ! -e "${psu_init_path}/psu_init.c" ]; then
      warn "Warning: Unable to find psu_init.c in ${psu_init_path}"
    fi
    if [ ! -e "${psu_init_path}/psu_init.h" ]; then
      warn "Warning: Unable to find psu_init.h in ${psu_init_path}"
    fi

    cat <<EOF >"${conf_file}"
PSU_INIT_PATH = "${psu_init_path}"
EOF
  else
    cat /dev/null >"${conf_file}"
  fi
  cat <<EOF >>"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$3"
DEFAULTTUNE = "cortexr5"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "$yocto_distro"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_r5_freertos() {
  info "cortex-r5 FreeRTOS configuration for core $2 [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexr5-$2-${machine}${suffix}-freertos.dtb"
  multiconf="${multiconf} cortexr5-$2-${machine}${suffix}-freertos"
  conf_file="multiconfig/cortexr5-$2-${machine}${suffix}-freertos.conf"
  libxil="multiconfig/includes/cortexr5-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexr5-${machine}${suffix}-distro.conf"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    if [ -n "$domain_file" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' \
        -i "${domain_file}" -i "${lops_dir}/lop-r5-imux.dts" "${system_dtb}" "${dtb_file}" \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-r5-imux.dts" \
        "${system_dtb}" "${dtb_file}" || error "lopper failed"
    fi
    rm -f lop-r5-imux.dts.dtb
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $3 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $3 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$3"
DEFAULTTUNE = "cortexr5"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

# Generate microblaze tunings
microblaze_done=0
process_microblaze() {
  [ ${microblaze_done} = 1 ] && return

  info "Generating microblaze processor tunes"

  mkdir -p machine/include
  (
    cd dtb || error "Unable to cd to dtb dir"
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-microblaze-yocto.dts" "${system_dtb}" \
      || error "lopper failed"
    rm -f lop-microblaze-yocto.dts.dtb
  ) >machine/include/${mach_conf}-microblaze.inc

  echo "require conf/machine/include/xilinx-microblaze.inc" >> machine/include/${mach_conf}-microblaze.inc

  microblaze_done=1
}

# pmu-microblaze is ALWAYS baremetal, no domain
pmu-microblaze() {
  info "Microblaze ZynqMP PMU"

  process_microblaze

  dtb_file="microblaze-0-pmu.dtb"
  multiconf="${multiconf} microblaze-0-pmu"
  multiconf_min="${multiconf_min} microblaze-0-pmu"
  conf_file="multiconfig/microblaze-0-pmu.conf"
  libxil="multiconfig/includes/microblaze-pmu-libxil.conf"
  distro="multiconfig/includes/microblaze-pmu-distro.conf"

  pmu_mcdepends="mc::${dtb_file%%.dtb}:pmu-firmware:do_deploy"
  pmu_firmware_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" "${dtb_file}" || error "lopper failed"
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $1 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $1 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$1"

DEFAULTTUNE = "microblaze-pmu"

TARGET_CFLAGS += "-DPSU_PMU=1U"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

# pmc-microblaze is ALWAYS baremetal, no domain
pmc-microblaze() {
  info "Microblaze Versal PMC"

  process_microblaze

  dtb_file="microblaze-0-pmc.dtb"
  multiconf="${multiconf} microblaze-0-pmc"
  multiconf_min="${multiconf_min} microblaze-0-pmc"
  conf_file="multiconfig/microblaze-0-pmc.conf"
  libxil="multiconfig/includes/microblaze-pmc-libxil.conf"
  distro="multiconfig/includes/microblaze-pmc-distro.conf"

  plm_mcdepends="mc::${dtb_file%%.dtb}:plm-firmware:do_deploy"
  plm_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" "${dtb_file}" || error "lopper failed"
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $1 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $1 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$1"

DEFAULTTUNE = "microblaze-pmc"

TARGET_CFLAGS += "-DVERSAL_PLM=1"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

# psm-microblaze is ALWAYS baremetal, no domain
psm-microblaze() {
  info "Microblaze Versal PSM"

  process_microblaze

  dtb_file="microblaze-0-psm.dtb"
  multiconf="${multiconf} microblaze-0-psm"
  multiconf_min="${multiconf_min} microblaze-0-psm"
  conf_file="multiconfig/microblaze-0-psm.conf"
  libxil="multiconfig/includes/microblaze-psm-libxil.conf"
  distro="multiconfig/includes/microblaze-psm-distro.conf"

  psm_mcdepends="mc::${dtb_file%%.dtb}:psm-firmware:do_deploy"
  psm_firmware_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"

  # Build device tree
  (
    cd dtb || error "Unable to cd to dtb dir"
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" "${dtb_file}" || error "lopper failed"
  )

  # Build baremetal multiconfig
  if [ -n "${domain_file}" ]; then
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx $1 "${embeddedsw}" || error "lopper failed"
  else
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx $1 "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"

ESW_MACHINE = "$1"

DEFAULTTUNE = "microblaze-psm"

TARGET_CFLAGS += "-DVERSAL_psm=1"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

generate_machine() {
  info "Generating machine conf file"
  conf_file="machine/${mach_conf}.conf"
  mkdir -p machine
  # Generate header
  cat <<EOF >"${conf_file}"
#@TYPE: Machine
#@NAME: ${mach_conf}
#@DESCRIPTION: ${model}

#### Preamble
MACHINEOVERRIDES =. "\${@['', '${mach_conf}:']['${mach_conf}' != '\${MACHINE}']}"
#### Regular settings follow

EOF

  if [ "${machine}" == "zynqmp" ]; then
    cat <<EOF >>"${conf_file}"
TUNEFILE[microblaze-pmu] = "conf/machine/include/${mach_conf}-microblaze.inc"
EOF
  elif [ "${machine}" == "versal" ]; then
    cat <<EOF >>"${conf_file}"
TUNEFILE[microblaze-pmc] = "conf/machine/include/${mach_conf}-microblaze.inc"
TUNEFILE[microblaze-psm] = "conf/machine/include/${mach_conf}-microblaze.inc"
EOF
  fi

  sysdt_path=$(dirname ${system_dtb})
  sysdt_base=$(basename ${system_dtb})
  cat <<EOF >>"${conf_file}"

# Set the default (linux) domain device tree
CONFIG_DTFILE ?= "\${TOPDIR}/conf/dtb/${system_conf}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

require conf/machine/${machine}-generic.conf

# System Device Tree does not use HDF_MACHINE
HDF_MACHINE = ""

# Set the system device trees
SYSTEM_DTFILE_DIR = "${sysdt_path}"
SYSTEM_DTFILE = "\${SYSTEM_DTFILE_DIR}/${sysdt_base}"
SYSTEM_DTFILE[vardepsexclude] += "SYSTEM_DTFILE_DIR"

EOF

  if [ -n "${fsbl_mcdepends}" ]; then
    cat <<EOF >>"${conf_file}"
# First Stage Boot Loader
FSBL_DEPENDS = ""
FSBL_MCDEPENDS = "${fsbl_mcdepends}"
FSBL_DEPLOY_DIR = "${fsbl_deploy_dir}"

EOF
  fi
  if [ -n "${r5fsbl_mcdepends}" ]; then
    cat <<EOF >>"${conf_file}"
# Cortex-R5 First Stage Boot Loader
R5FSBL_DEPENDS = ""
R5FSBL_MCDEPENDS = "${r5fsbl_mcdepends}"
R5FSBL_DEPLOY_DIR = "${r5fsbl_deploy_dir}"

EOF
  fi
  if [ -n "${pmu_mcdepends}" ]; then
    cat <<EOF >>"${conf_file}"
# PMU Firware
PMU_DEPENDS = ""
PMU_MCDEPENDS = "${pmu_mcdepends}"
PMU_FIRMWARE_DEPLOY_DIR = "${pmu_firmware_deploy_dir}"

EOF
  fi
  if [ -n "${plm_mcdepends}" ]; then
    cat <<EOF >>"${conf_file}"
# Platform Loader and Manager
PLM_DEPENDS = ""
PLM_MCDEPENDS = "${plm_mcdepends}"
PLM_DEPLOY_DIR = "${plm_deploy_dir}"

EOF
  fi
  if [ -n "${psm_mcdepends}" ]; then
    cat <<EOF >>"${conf_file}"
# PSM Firmware
PSM_DEPENDS = ""
PSM_MCDEPENDS = "${psm_mcdepends}"
PSM_FIRMWARE_DEPLOY_DIR = "${psm_firmware_deploy_dir}"

EOF
  fi

  if [ -n "${full_pdi_path}" ]; then
    pdi_path_dir=$(dirname ${full_pdi_path})
    pdi_path_base=$(basename ${full_pdi_path})
    cat <<EOF >>"${conf_file}"
# Versal PDI
PDI_PATH_DIR = "${pdi_path_dir}"
PDI_PATH = "\${PDI_PATH_DIR}/${pdi_path_base}"
PDI_PATH[vardepsexclude] += "PDI_PATH_DIR"

EOF
  fi

  cat <<EOF >>"${conf_file}"
# Enable the correct version of the firmware components
PREFERRED_VERSION_fsbl-firmware = "2023.1_sdt_experimental%"
PREFERRED_VERSION_pmu-firmware = "2023.1_sdt_experimental%"
PREFERRED_VERSION_plm-firmware = "2023.1_sdt_experimental%"
PREFERRED_VERSION_psm-firmware = "2023.1_sdt_experimental%"

# Exclude BASE_TMPDIR from hash calculations
BB_HASHEXCLUDE_COMMON:append = " BASE_TMPDIR"

# We don't want the kernel to build us a device-tree
KERNEL_DEVICETREE:example-sdt = ""
# We need u-boot to use the one we passed in
DEVICE_TREE_NAME:pn-u-boot-xlnx-scr = "\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Update bootbin to use proper device tree
BIF_PARTITION_IMAGE[device-tree] = "\${RECIPE_SYSROOT}/boot/devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Remap boot files to ensure the right device tree is listed first
IMAGE_BOOT_FILES =+ "devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"

#### No additional settings should be after the Postamble
#### Postamble
PACKAGE_EXTRA_ARCHS:append = "\${@['', ' ${mach_conf//-/_}']['${mach_conf}' != "\${MACHINE}"]}"
EOF
}

parse_cpus() {
  gen_linux_dtb="None"
  while read -r cpu core domain cpu_name os_hint; do
    # Skip commented lines and WARNINGs
    case ${cpu} in
      \#* | \[WARNING\]:) continue ;;
    esac

    case ${cpu} in

      arm,cortex-a53)
        # We need a base cortex_a53_baremetal for the FSBL
        if [ "${core}" == 0 ]; then
          cortex_a53_baremetal fsbl ${core} ${cpu_name}
	fi
        if [ "${os_hint}" == "None" ]; then
          if [ "${gen_linux_dtb}" == "None" ]; then
            cortex_a53_linux "${domain}"
	    gen_linux_dtb="True"
	  fi
          cortex_a53_baremetal "${domain}" ${core} ${cpu_name}
          cortex_a53_freertos "${domain}" ${core} ${cpu_name}
        else
          case "${os_hint}" in
            linux*)
              if [ "${gen_linux_dtb}" == "None" ]; then
                cortex_a53_linux "${domain}"
		gen_linux_dtb="True"
	      fi
	      ;;
            baremetal*)
              cortex_a53_baremetal "${domain}" ${core} ${cpu_name};;
            freertos*)
              cortex_a53_freertos "${domain}" ${core} ${cpu_name};;
            *)
              warn "cortex-a53 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_a53_baremetal "${domain}"
          esac
        fi
        ;;

      arm,cortex-a72)
        if [ "${os_hint}" == "None" ]; then
          if [ "${gen_linux_dtb}" == "None" ]; then
            cortex_a72_linux "${domain}"
	    gen_linux_dtb="True"
	  fi
          cortex_a72_baremetal "${domain}" ${core} ${cpu_name}
          cortex_a72_freertos "${domain}" ${core} ${cpu_name}
        else
          case "${os_hint}" in
            linux*)
              if [ "${gen_linux_dtb}" == "None" ]; then
                cortex_a72_linux "${domain}"
	        gen_linux_dtb="True"
	      fi
	      ;;
            baremetal*)
              cortex_a72_baremetal "${domain}" ${core} ${cpu_name};;
            freertos*)
              cortex_a72_freertos "${domain}" ${core} ${cpu_name};;
            *)
              warn "cortex-a72 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_a72_baremetal "${domain}"
          esac
        fi
        ;;
      arm,cortex-r5)
        if [ "${os_hint}" == "None" ]; then
          # We need a base cortex_r5_baremetal for the FSBL for ZynqMP platform
          [ "${machine}" = "zynqmp" ] && cortex_r5_baremetal fsbl ${core} ${cpu_name}
          cortex_r5_baremetal "${domain}" ${core} ${cpu_name}
          cortex_r5_freertos "${domain}" ${core} ${cpu_name}
        else
          case "${os_hint}" in
            baremetal*)
              cortex_r5_baremetal "${domain}" ${core} ${cpu_name};;
            freertos*)
              cortex_r5_freertos "${domain}" ${core} ${cpu_name};;
            *)
              warn "cortex-r5 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_r5_baremetal "${domain}"
          esac
        fi
        ;;

      xlnx,microblaze)
        process_microblaze
        case "${os_hint}" in
          None | baremetal*)
            warn "Microblaze for Baremetal ${domain} not yet implemented" ;;
          Linux)
            warn "Microblaze for Linux ${domain} not yet implemented" ;;
          *)
            warn "Microblaze for unknown OS (${os_hint}), not yet implemented. ${domain}" ;;
        esac
        ;;

      pmu-microblaze)
        pmu-microblaze ${cpu_name};;

      pmc-microblaze)
        pmc-microblaze ${cpu_name};;

      psm-microblaze)
        psm-microblaze ${cpu_name};;
      *)
        warn "Unknown CPU ${cpu}"

    esac
  done <${cpulist}
}

gen_local_conf() {
  cat << EOF >> $1

# Avoid errors in some baremetal configs as these layers may be present
# but are not used.  Note the following lines are optional and can be
# safetly disabled.
SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"

# Each multiconfig will define it's own TMPDIR, this is the new default based
# on BASE_TMPDIR for the Linux build
TMPDIR = "\${BASE_TMPDIR}/tmp"

# Use the newly generated MACHINE
MACHINE = "${mach_conf}"

# All of the TMPDIRs must be in a common parent directory. This is defined
# as BASE_TMPDIR.
# Adjust BASE_TMPDIR if you want to move the tmpdirs elsewhere, such as /tmp
BASE_TMPDIR ?= "\${TOPDIR}"

# The following is the full set of multiconfigs for this configuration
# A large list can cause a slow parse.
BBMULTICONFIG = "${multiconf}"
# Alternatively trim the list to the minimum
#BBMULTICONFIG = "${multiconf_min}"
EOF
}

gen_petalinux_conf() {
  cd "${config_dir}" || exit
  (
    if [ "$machine" == "zynqmp" ]; then
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} "${system_dtb}" -- petalinuxconfig_xlnx psu_cortexa53_0 ${petalinux_schema} \
        || error "lopper failed"
    else
      LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} "${system_dtb}" -- petalinuxconfig_xlnx psv_cortexa72_0 ${petalinux_schema} \
        || error "lopper failed"
    fi
  )
}
parse_args "$@"

lopper=$(command -v lopper)
lopper_dir=$(dirname "${lopper}")
lops_dir=$(ls -d $(dirname "${lopper_dir}")/lib/python*/site-packages/lopper/lops | head -n 1)
embeddedsw=$(dirname "${lopper_dir}")/share/embeddedsw
system_conf=""
multiconf=""

[ -z "${lopper}" ] && error "Unable to find lopper, please source the prestep environment"

cpulist=$(mktemp)

priordir=$(pwd)
cd "${config_dir}" || exit
mkdir -p dtb multiconfig/includes
# Get mach_conf name and model name
(
  cd dtb || error "Unable to cd to dtb dir"
  LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-machine-name.dts" "${system_dtb}" \
    /dev/null > ${cpulist} || error "lopper failed"
  rm -f "lop-machine-name.dts.dtb"
)
read local_mach_conf model < ${cpulist}
if [ -z "${mach_conf}" ]; then
    mach_conf=${local_mach_conf}
fi

# Generate CPU list
(
  cd dtb || error "Unable to cd to dtb dir"
  LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} -f --enhanced -i "${lops_dir}/lop-xilinx-id-cpus.dts" "${system_dtb}" \
    /dev/null > ${cpulist} || error "lopper failed"
  rm -f "lop-xilinx-id-cpus.dts.dtb"
)

detect_machine

echo "System Configuration:"
echo "MODEL       = \"${model}\""
echo "MACHINE     = \"${mach_conf}\""
echo "SOC_FAMILY  = \"${machine}\""
echo "CPUs:"
dump_cpus "            = "
echo

info "Generating configuration..."
parse_cpus

generate_machine

cd ${priordir}
if [ -z "${localconf}" ]; then
  echo
  echo "To enable this, add the following to your local.conf:"
  echo
  tmpfile=$(mktemp)
  gen_local_conf ${tmpfile}
  cat $tmpfile
  rm $tmpfile
else
  echo
  echo "Configuration for local.conf written to ${localconf}"
  echo
  gen_local_conf ${localconf}
fi

if [ -n "${petalinux_schema}" ]; then
  echo
  echo "Generating petalinux config file:"
  echo
  gen_petalinux_conf
fi

# Cleanup our temp file
rm -rf ${cpulist} ${config_dir}/CMakeLists.txt ${config_dir}/DRVLISTConfig.cmake
