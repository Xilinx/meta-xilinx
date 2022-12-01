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
    [-m <machine>]          zynqmp or versal
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
      m) machine=$OPTARG ;;
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
    while read -r cpu domain os_hint; do
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
    error "Unable to autodetect machine type, use -m to specify the machine."

  case ${machine} in
    zynqmp | versal) : ;;
    *) error "Invalid machine type ${machine}; please choose zynqmp or versal"
  esac
}

cortex_a53_linux() {
  info "cortex-a53 for Linux [ $1 ]"

  if [ "$1" = "None" ]; then
    dtb_file="cortexa53-${machine}-linux.dtb"
    dts_file="cortexa53-${machine}-linux.dts"
    system_conf=conf/cortexa53-${machine}-linux.conf
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

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

MACHINE = "${machine}-generic"
# We don't want the kernel to build us a device-tree
KERNEL_DEVICETREE:${machine}-generic = ""
# We need u-boot to use the one we passed in
DEVICE_TREE_NAME:pn-u-boot-xlnx-scr = "\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Update bootbin to use proper device tree
BIF_PARTITION_IMAGE[device-tree] = "\${RECIPE_SYSROOT}/boot/devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Remap boot files to ensure the right device tree is listed first
IMAGE_BOOT_FILES = "devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))} \${@get_default_image_boot_files(d)}"
EOF
}

a53_fsbl_done=0
cortex_a53_baremetal() {
  if [ "$1" = "fsbl" ]; then
    [ ${a53_fsbl_done} = 1 ] && return
    info "cortex-a53 FSBL baremetal configuration"
  else
    info "cortex-a53 for baremetal [ $1 ]"
  fi

  suffix=""; lto="-nolto"
  if [ "$1" != "None" ]; then
    suffix="-$1"; lto=""
  fi

  dtb_file="cortexa53-${machine}${suffix}-baremetal.dtb"
  multiconf="${multiconf} cortexa53-${machine}${suffix}-baremetal"
  conf_file="multiconfig/cortexa53-${machine}${suffix}-baremetal.conf"
  libxil="multiconfig/includes/cortexa53-${machine}${suffix}-libxil.conf"
  distro="multiconfig/includes/cortexa53-${machine}${suffix}-distro.conf"
  yocto_distro="xilinx-standalone${lto}"
  if [ "$1" = "fsbl" ]; then
    fsbl_mcdepends="mc::${dtb_file%%.dtb}:fsbl-firmware:do_deploy"
    fsbl_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"
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
    ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx cortexa53-${machine} "${embeddedsw}" \
      || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx cortexa53-${machine} "${embeddedsw}" \
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
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "cortexa53-${machine}"
DEFAULTTUNE = "cortexa53"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "${yocto_distro}"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

cortex_a53_freertos() {
  info "cortex-a53 for FreeRTOS [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexa53-${machine}${suffix}-freertos.dtb"
  multiconf="${multiconf} cortexa53-${machine}${suffix}-freertos"
  conf_file="multiconfig/cortexa53-${machine}${suffix}-freertos.conf"
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
    ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx cortexa53-${machine} "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx cortexa53-${machine} "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "cortexa53-${machine}"
DEFAULTTUNE = "cortexa53"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

cortex_a72_linux() {
  info "cortex-a72 for Linux [ $1 ]"

  # Find the first file ending in .pdi
  full_pdi_path=$(ls ${pdi_path}/*.pdi 2>/dev/null | head -n 1)
  if [ -z "${full_pdi_path}" ]; then
    warn "Warning: Unable to find a pdi file in ${pdi_path}"
    full_pdi_path="__PATH TO PDI FILE HERE__"
  elif [ "${full_pdi_path}" != "$(ls ${pdi_path}/*.pdi 2>/dev/null)" ]; then
    warn "Warning: multiple PDI files found, using first found $(basename ${full_pdi_path})."
  fi

  if [ "$1" = "None" ]; then
    dtb_file="cortexa72-${machine}-linux.dtb"
    dts_file="cortexa72-${machine}-linux.dts"
    system_conf=conf/cortexa72-${machine}-linux.conf
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

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

MACHINE = "${machine}-generic"
# We don't want the kernel to build us a device-tree
KERNEL_DEVICETREE:${machine}-generic = ""
# We need u-boot to use the one we passed in
DEVICE_TREE_NAME:pn-u-boot-xlnx-scr = "\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Update bootbin to use proper device tree
BIF_PARTITION_IMAGE[device-tree] = "\${RECIPE_SYSROOT}/boot/devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Remap boot files to ensure the right device tree is listed first
IMAGE_BOOT_FILES = "devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))} \${@get_default_image_boot_files(d)}"
EOF
}

cortex_a72_baremetal() {
  info "cortex-a72 for baremetal [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexa72-${machine}${suffix}-baremetal.dtb"
  multiconf="${multiconf} cortexa72-${machine}${suffix}-baremetal"
  conf_file="multiconfig/cortexa72-${machine}${suffix}-baremetal.conf"
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
    ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx cortexa72-${machine} "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx cortexa72-${machine} "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "cortexa72-${machine}"
DEFAULTTUNE = "cortexa72"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone-nolto"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

cortex_a72_freertos() {
  info "cortex-a72 for FreeRTOS [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexa72-${machine}${suffix}-freertos.dtb"
  multiconf="${multiconf} cortexa72-${machine}${suffix}-freertos"
  conf_file="multiconfig/cortexa72-${machine}${suffix}-freertos.conf"
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
    ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx cortexa72-${machine} "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx cortexa72-${machine} "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "cortexa72-${machine}"
DEFAULTTUNE = "cortexa72"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

r5_fsbl_done=0
cortex_r5_baremetal() {
  if [ "$1" = "fsbl" ]; then
    [ ${r5_fsbl_done} = 1 ] && return
    info "cortex-r5 FSBL baremetal configuration"
  else
    info "cortex-r5 for baremetal [ $1 ]"
  fi

  suffix=""; lto="-nolto"
  if [ "$1" != "None" ]; then
    suffix="-$1"; lto=""
  fi

  dtb_file="cortexr5-${machine}${suffix}-baremetal.dtb"
  multiconf="${multiconf} cortexr5-${machine}${suffix}-baremetal"
  conf_file="multiconfig/cortexr5-${machine}${suffix}-baremetal.conf"
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
    ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx cortexr5-${machine} "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx cortexr5-${machine} "${embeddedsw}" \
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
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "cortexr5-${machine}"
DEFAULTTUNE = "cortexr5"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "$yocto_distro"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

cortex_r5_freertos() {
  info "cortex-r5 for FreeRTOS [ $1 ]"

  suffix=""
  [ "$1" != "None" ] && suffix="-$1"

  dtb_file="cortexr5-${machine}${suffix}-freertos.dtb"
  multiconf="${multiconf} cortexr5-${machine}${suffix}-freertos"
  conf_file="multiconfig/cortexr5-${machine}${suffix}-freertos.conf"
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
    ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx cortexr5-${machine} "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx cortexr5-${machine} "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "cortexr5-${machine}"
DEFAULTTUNE = "cortexr5"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

# Generate microblaze tunings
microblaze_done=0
process_microblaze() {
  [ ${microblaze_done} = 1 ] && return

  info "Generating microblaze processor tunes"

  (
    cd dtb || error "Unable to cd to dtb dir"
    ${lopper} -f --enhanced -i "${lops_dir}/lop-microblaze-yocto.dts" "${system_dtb}" \
      || error "lopper failed"
    rm -f lop-microblaze-yocto.dts.dtb
  ) >microblaze.conf

  microblaze_done=1
}

# pmu-microblaze is ALWAYS baremetal, no domain
pmu-microblaze() {
  info "Microblaze ZynqMP PMU"

  process_microblaze

  dtb_file="microblaze-pmu.dtb"
  multiconf="${multiconf} microblaze-pmu"
  conf_file="multiconfig/microblaze-pmu.conf"
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
    ${lopper} -f --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx microblaze-pmu "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx microblaze-pmu "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "microblaze-pmu"

require conf/microblaze.conf
DEFAULTTUNE = "microblaze"
TUNE_FEATURES:tune-microblaze:forcevariable = "\${TUNE_FEATURES:tune-pmu-microblaze}"

TARGET_CFLAGS += "-DPSU_PMU=1U"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

# pmc-microblaze is ALWAYS baremetal, no domain
pmc-microblaze() {
  info "Microblaze Versal PMC"

  process_microblaze

  dtb_file="microblaze-pmc.dtb"
  multiconf="${multiconf} microblaze-pmc"
  conf_file="multiconfig/microblaze-pmc.conf"
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
    ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx microblaze-plm "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx microblaze-plm "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "microblaze-plm"

require conf/microblaze.conf
DEFAULTTUNE = "microblaze"
TUNE_FEATURES:tune-microblaze:forcevariable = "\${TUNE_FEATURES:tune-pmc-microblaze}"

TARGET_CFLAGS += "-DVERSAL_PLM=1"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

# psm-microblaze is ALWAYS baremetal, no domain
psm-microblaze() {
  info "Microblaze Versal PSM"

  process_microblaze

  dtb_file="microblaze-psm.dtb"
  multiconf="${multiconf} microblaze-psm"
  conf_file="multiconfig/microblaze-psm.conf"
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
    ${lopper} -f  --enhanced -x '*.yaml' -i "${domain_file}" "${system_dtb}" \
      -- baremetaldrvlist_xlnx microblaze-psm "${embeddedsw}" || error "lopper failed"
  else
    ${lopper} -f "${system_dtb}" -- baremetaldrvlist_xlnx microblaze-psm "${embeddedsw}" \
      || error "lopper failed"
  fi

  mv libxil.conf "${libxil}"
  mv distro.conf "${distro}"

  cat <<EOF >"${conf_file}"
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
CONFIG_DTFILE[vardepsexclude] += "TOPDIR"

ESW_MACHINE = "microblaze-psm"

require conf/microblaze.conf
DEFAULTTUNE = "microblaze"
TUNE_FEATURES:tune-microblaze:forcevariable = "\${TUNE_FEATURES:tune-psm-microblaze}"

TARGET_CFLAGS += "-DVERSAL_psm=1"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}

SKIP_META_VIRT_SANITY_CHECK = "1"
SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"
EOF
}

parse_cpus() {
  info "Generating configuration..."

  while read -r cpu domain os_hint; do

    # Skip commented lines and WARNINGs
    case ${cpu} in
      \#* | \[WARNING\]:) continue ;;
    esac

    case ${cpu} in

      arm,cortex-a53)
        # We need a base cortex_a53_baremetal for the FSBL
        cortex_a53_baremetal fsbl
        if [ "${os_hint}" == "None" ]; then
          cortex_a53_linux "${domain}"
          cortex_a53_baremetal "${domain}"
          cortex_a53_freertos "${domain}"
        else
          case "${os_hint}" in
            linux*)
              cortex_a53_linux "${domain}" ;;
            baremetal*)
              cortex_a53_baremetal "${domain}" ;;
            freertos*)
              cortex_a53_freertos "${domain}" ;;
            *)
              warn "cortex-a53 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_a53_baremetal "${domain}"
          esac
        fi
        ;;

      arm,cortex-a72)
        if [ "${os_hint}" == "None" ]; then
          cortex_a72_linux "${domain}"
          cortex_a72_baremetal "${domain}"
          cortex_a72_freertos "${domain}"
        else
          case "${os_hint}" in
            linux*)
              cortex_a72_linux "${domain}" ;;
            baremetal*)
              cortex_a72_baremetal "${domain}" ;;
            freertos*)
              cortex_a72_freertos "${domain}" ;;
            *)
              warn "cortex-a72 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_a72_baremetal "${domain}"
          esac
        fi
        ;;

      arm,cortex-r5)
        if [ "${os_hint}" == "None" ]; then
          # We need a base cortex_r5_baremetal for the FSBL for ZynqMP platform
          [ "${machine}" = "zynqmp" ] && cortex_r5_baremetal fsbl
          cortex_r5_baremetal "${domain}"
          cortex_r5_freertos "${domain}"
        else
          case "${os_hint}" in
            baremetal*)
              cortex_r5_baremetal "${domain}" ;;
            freertos*)
              cortex_r5_freertos "${domain}" ;;
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
        pmu-microblaze ;;

      pmc-microblaze)
        pmc-microblaze ;;

      psm-microblaze)
        psm-microblaze ;;

      *)
        warn "Unknown CPU ${cpu}"

    esac
  done <${cpulist}
}

gen_local_conf() {
  echo "# Adjust BASE_TMPDIR if you want to move the tmpdirs elsewhere" >> $1
  echo "BASE_TMPDIR ?= \"\${TOPDIR}\"" >> $1
  [ -n "${system_conf}" ] && echo "require ${system_conf}" >> $1
  echo "SYSTEM_DTFILE = \"${system_dtb}\"" >> $1
  echo "BBMULTICONFIG += \"${multiconf}\"" >> $1
  if [ -n "${fsbl_mcdepends}" ]; then
    echo "FSBL_DEPENDS = \"\"" >> $1
    echo "FSBL_MCDEPENDS = \"${fsbl_mcdepends}\"" >> $1
    echo "FSBL_DEPLOY_DIR = \"${fsbl_deploy_dir}\"" >> $1
  fi
  if [ -n "${r5fsbl_mcdepends}" ]; then
    echo "R5FSBL_DEPENDS = \"\"" >> $1
    echo "R5FSBL_MCDEPENDS = \"${r5fsbl_mcdepends}\"" >> $1
    echo "R5FSBL_DEPLOY_DIR = \"${r5fsbl_deploy_dir}\"" >> $1
  fi
  if [ -n "${pmu_mcdepends}" ]; then
    echo "PMU_DEPENDS = \"\"" >> $1
    echo "PMU_MCDEPENDS = \"${pmu_mcdepends}\"" >> $1
    echo "PMU_FIRMWARE_DEPLOY_DIR = \"${pmu_firmware_deploy_dir}\"" >> $1
  fi
  if [ -n "${plm_mcdepends}" ]; then
    echo "PLM_DEPENDS = \"\"" >> $1
    echo "PLM_MCDEPENDS = \"${plm_mcdepends}\"" >> $1
    echo "PLM_DEPLOY_DIR = \"${plm_deploy_dir}\"" >> $1
  fi
  if [ -n "${psm_mcdepends}" ]; then
    echo "PSM_DEPENDS = \"\"" >> $1
    echo "PSM_MCDEPENDS = \"${psm_mcdepends}\"" >> $1
    echo "PSM_FIRMWARE_DEPLOY_DIR = \"${psm_firmware_deploy_dir}\"" >> $1
  fi
  [ "${machine}" = "versal" ] && echo "PDI_PATH = \"${full_pdi_path}\"" >> $1
  echo
}

gen_petalinux_conf() {
  cd "${config_dir}" || exit
  (
    LOPPER_DTC_FLAGS="-b 0 -@" ${lopper} "${system_dtb}" -- petalinuxconfig_xlnx ${petalinux_schema} \
      || error "lopper failed"
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
# Generate CPU list
cd "${config_dir}" || exit
mkdir -p dtb multiconfig/includes
(
  cd dtb || error "Unable to cd to dtb dir"
  ${lopper} -f --enhanced -i "${lops_dir}/lop-xilinx-id-cpus.dts" "${system_dtb}" \
    /dev/null > ${cpulist} || error "lopper failed"
  rm -f "lop-xilinx-id-cpus.dts.dtb"
)

detect_machine

parse_cpus

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
rm ${cpulist}
