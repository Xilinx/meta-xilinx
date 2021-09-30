#! /bin/bash

if [ $# -lt 2 ]; then
  echo "Usage:"
  echo "  $0 <conf_dir> <system_dtb> [<overlay_dtb>] [<external_fpga>] [<machine_type>]"
  echo
  echo "  conf_dir - location for the build conf directory"
  echo "  system_dtb - Full patch to the system dtb"
  echo "  overlay_dtb - To generate overlay dts"
  echo "  external_fpga - Flag to apply partial overlay"
  echo "  machine_type - zynq, zynqmp or versal"
  echo
  exit 1
fi

if [ ! -f $1/local.conf ]; then
  echo "$1 does not look like a conf directory."
  exit 1
fi
conf_dir=$1

if [ ! -f $2 ]; then
  echo "Unable to find $2." >&2
  exit 1
fi
dtb=$2

# Default is no overlay
overlay_dtb=false
external_fpga=false
if [ ! -f $3 ]; then
  if [ ! -f $4 ]; then
    overlay_dtb=true
    external_fpga=true
    machine=$5
  else
    overlay_dtb=true
    machine=$4
  fi
else
  # We'll autodetect if blank (later)
  machine=$3
fi

lopper=$(which lopper.py)

if [ -z "$lopper" ]; then
  echo "Unable to find lopper.py, did you source the prestep environment file?" >&2
  exit 1
fi

lops_dir=$(dirname $(dirname $lopper))/share/lopper/lops
embeddedsw=$(dirname $(dirname $lopper))/share/embeddedsw

system_conf=""
multiconf=""

detect_machine() {
  # Lets identify the system type first.  We can use PSM/PMC/PMU for this...
  while read cpu domain os_hint ; do
    case ${cpu} in
      pmu-microblaze)
        machine="zynqmp"
        return
        ;;
      pmc-microblaze | psm-microblaze)
        machine="versal"
        return
        ;;
    esac
  done < cpu-list.tmp
  echo "ERROR: Unable to auto-detect the machine type."
  exit 1
}

cortex_a53_linux() {
  if [ $1 = "None" ]; then
    dtb_file="cortexa53-${machine}-linux.dtb"
    system_conf=conf/cortexa53-${machine}-linux.conf
    conf_file=cortexa53-${machine}-linux.conf
  else
    dtb_file="cortexa53-${machine}-${1}-linux.dtb"
    multiconf="${multiconf} cortexa53-${machine}-linux"
    conf_file=multiconfig/cortexa53-${machine}-${1}-linux.conf
  fi

  mkdir -p dtb
  # Check if it is overlay dts otherwise just create linux dtb
  if [ ${overlay_dtb} = "true" ]; then
    if [ ${external_fpga} = "true" ]; then
      (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f ${dtb} -- xlnx_overlay_dt ${machine} full; dtc -q -O dtb -o pl.dtbo -b 0 -@ pl.dtsi && rm -f pl.dtsi)
    else
      (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py ${dtb} -- xlnx_overlay_dt ${machine} partial ; dtc -q -O dtb -o pl.dtbo -b 0 -@ pl.dtsi && rm -f pl.dtsi)
    fi
  else
   (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-a53-imux.dts -i ${lops_dir}/lop-domain-linux-a53.dts ${dtb} ${dtb_file} && rm -f lop-a53-imux.dts.dtb lop-domain-linux-a53.dts.dtb)
  fi

  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
MACHINE = "${machine}-generic"
# Override the SYSTEM_DTFILE for Linux builds
SYSTEM_DTFILE_linux = "\${CONFIG_DTFILE}"
# We don't want the kernel to build us a device-tree
KERNEL_DEVICETREE_${machine}-generic = ""
# We need u-boot to use the one we passed in
DEVICE_TREE_NAME_pn-u-boot-zynq-scr = "\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Update bootbin to use proper device tree
BIF_PARTITION_IMAGE[device-tree] = "\${RECIPE_SYSROOT}/boot/devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Remap boot files to ensure the right device tree is listed first
IMAGE_BOOT_FILES = "devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))} \${@get_default_image_boot_files(d)}"
EOF
}

fsbl_done=0
cortex_a53_baremetal() {
  if [ $1 = "fsbl" -a $fsbl_done != 0 ]; then
    return
  elif [ $1 = "fsbl" ] ; then
    echo "Building FSBL baremetal configuration"
    fsbl_done=1
  fi
  if [ $1 = "None" ]; then
    dtb_file="cortexa53-${machine}-baremetal.dtb"
    multiconf="${multiconf} cortexa53-${machine}-baremetal"
    conf_file="multiconfig/cortexa53-${machine}-baremetal.conf"
    libxil="multiconfig/includes/cortexa53-${machine}-libxil.conf"
    distro="multiconfig/includes/cortexa53-${machine}-distro.conf"
    yocto_distro="xilinx-standalone-nolto"
  else
    dtb_file="cortexa53-${machine}-${1}-baremetal.dtb"
    multiconf="${multiconf} cortexa53-${machine}-${1}-baremetal"
    conf_file="multiconfig/cortexa53-${machine}-${1}-baremetal.conf"
    libxil="multiconfig/includes/cortexa53-${machine}-${1}-libxil.conf"
    distro="multiconfig/includes/cortexa53-${machine}-${1}-distro.conf"
    yocto_distro="xilinx-standalone"
  fi
  if [ $1 = "fsbl" ]; then
    fsbl_mcdepends="mc::${dtb_file%%.dtb}:fsbl-firmware:do_deploy"
    fsbl_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"
  fi

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-a53-imux.dts ${dtb} ${dtb_file} && rm -f lop-a53-imux.dts.dtb)

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx cortexa53-${machine} $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "cortexa53-${machine}"
DEFAULTTUNE = "cortexa53"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "$yocto_distro"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_a53_freertos() {
  if [ $1 = "None" ]; then
    dtb_file="cortexa53-${machine}-freertos.dtb"
    multiconf="${multiconf} cortexa53-${machine}-freertos"
    conf_file="multiconfig/cortexa53-${machine}-freertos.conf"
    libxil="multiconfig/includes/cortexa53-${machine}-libxil.conf"
    distro="multiconfig/includes/cortexa53-${machine}-distro.conf"
  else
    dtb_file="cortexa53-${machine}-${1}-freertos.dtb"
    multiconf="${multiconf} cortexa53-${machine}-${1}-freertos"
    conf_file="multiconfig/cortexa53-${machine}-${1}-freertos.conf"
    libxil="multiconfig/includes/cortexa53-${machine}-${1}-libxil.conf"
    distro="multiconfig/includes/cortexa53-${machine}-${1}-distro.conf"
  fi

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-a53-imux.dts ${dtb} ${dtb_file} && rm -f lop-a53-imux.dts.dtb)

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx cortexa53-${machine} $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "cortexa53-${machine}"
DEFAULTTUNE = "cortexa53"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_a72_linux() {
  if [ $1 = "None" ]; then
    dtb_file="cortexa72-${machine}-linux.dtb"
    system_conf=conf/cortexa72-${machine}-linux.conf
    conf_file=cortexa72-${machine}-linux.conf
  else
    dtb_file="cortexa72-${machine}-${1}-linux.dtb"
    multiconf="${multiconf} cortexa72-${machine}-linux"
    conf_file=multiconfig/cortexa72-${machine}-${1}-linux.conf
  fi

  mkdir -p dtb
  # Check if it is overlay dts otherwise just create linux dtb
  if [ ${overlay_dtb} = "true" ]; then
    # As there is no partial support on Versal, As per fpga manager implementatin there is a flag "external_fpga" which says
    # apply overlay without loading the bit file.
    if [ ${external_fpga} = "true" ]; then
      (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f ${dtb} -- xlnx_overlay_dt ${machine} full external_fpga ; dtc -q -O dtb -o pl.dtbo -b 0 -@ pl.dtsi && rm -f pl.dtsi)
    else
      # If there is no external_fpga flag, then the default is full
      (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py ${dtb} -- xlnx_overlay_dt ${machine} full ; dtc -q -O dtb -o pl.dtbo -b 0 -@ pl.dtsi && rm -f pl.dtsi)
    fi
  else
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-a72-imux.dts -i ${lops_dir}/lop-domain-a72.dts ${dtb} ${dtb_file} && rm -f lop-a72-imux.dts.dtb lop-domain-a72.dts.dtb)
  fi

  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
MACHINE = "${machine}-generic"
# Override the SYSTEM_DTFILE for Linux builds
SYSTEM_DTFILE_linux = "\${CONFIG_DTFILE}"
# We don't want the kernel to build us a device-tree
KERNEL_DEVICETREE_${machine}-generic = ""
# We need u-boot to use the one we passed in
DEVICE_TREE_NAME_pn-u-boot-zynq-scr = "\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Update bootbin to use proper device tree
BIF_PARTITION_IMAGE[device-tree] = "\${RECIPE_SYSROOT}/boot/devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))}"
# Remap boot files to ensure the right device tree is listed first
IMAGE_BOOT_FILES = "devicetree/\${@os.path.basename(d.getVar('CONFIG_DTFILE'))} \${@get_default_image_boot_files(d)}"
EOF
}

cortex_a72_baremetal() {
  if [ $1 = "None" ]; then
    dtb_file="cortexa72-${machine}-baremetal.dtb"
    multiconf="${multiconf} cortexa72-${machine}-baremetal"
    conf_file="multiconfig/cortexa72-${machine}-baremetal.conf"
    libxil="multiconfig/includes/cortexa72-${machine}-libxil.conf"
    distro="multiconfig/includes/cortexa72-${machine}-distro.conf"
  else
    dtb_file="cortexa72-${machine}-${1}-baremetal.dtb"
    multiconf="${multiconf} cortexa72-${machine}-${1}-baremetal"
    conf_file="multiconfig/cortexa72-${machine}-${1}-baremetal.conf"
    libxil="multiconfig/includes/cortexa72-${machine}-${1}-libxil.conf"
    distro="multiconfig/includes/cortexa72-${machine}-${1}-distro.conf"
  fi

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-a72-imux.dts ${dtb} ${dtb_file} && rm -f lop-a72-imux.dts.dtb)

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx cortexa72-${machine} $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "cortexa72-${machine}"
DEFAULTTUNE = "cortexa72"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone-nolto"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_a72_freertos() {
  if [ $1 = "None" ]; then
    dtb_file="cortexa72-${machine}-freertos.dtb"
    multiconf="${multiconf} cortexa72-${machine}-freertos"
    conf_file="multiconfig/cortexa72-${machine}-freertos.conf"
    libxil="multiconfig/includes/cortexa72-${machine}-libxil.conf"
    distro="multiconfig/includes/cortexa72-${machine}-distro.conf"
  else
    dtb_file="cortexa72-${machine}-${1}-freertos.dtb"
    multiconf="${multiconf} cortexa72-${machine}-${1}-freertos"
    conf_file="multiconfig/cortexa72-${machine}-${1}-freertos.conf"
    libxil="multiconfig/includes/cortexa72-${machine}-${1}-libxil.conf"
    distro="multiconfig/includes/cortexa72-${machine}-${1}-distro.conf"
  fi

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-a72-imux.dts ${dtb} ${dtb_file} && rm -f lop-a72-imux.dts.dtb)

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx cortexa72-${machine} $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "cortexa72-${machine}"
DEFAULTTUNE = "cortexa72"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

r5_fsbl_done=0
cortex_r5_baremetal() {
  if [ $1 = "fsbl" -a $r5_fsbl_done != 0 ]; then
    return
  elif [ $1 = "fsbl" ] ; then
    echo "Building R5 FSBL baremetal configuration"
    r5_fsbl_done=1
  fi

  if [ $1 = "None" ]; then
    dtb_file="cortexr5-${machine}-baremetal.dtb"
    multiconf="${multiconf} cortexr5-${machine}-baremetal"
    conf_file="multiconfig/cortexr5-${machine}-baremetal.conf"
    libxil="multiconfig/includes/cortexr5-${machine}-libxil.conf"
    distro="multiconfig/includes/cortexr5-${machine}-distro.conf"
    yocto_distro="xilinx-standalone-nolto"
  else
    dtb_file="cortexr5-${machine}-${1}-baremetal.dtb"
    multiconf="${multiconf} cortexr5-${machine}-${1}-baremetal"
    conf_file="multiconfig/cortexr5-${machine}-${1}-baremetal.conf"
    libxil="multiconfig/includes/cortexr5-${machine}-${1}-libxil.conf"
    distro="multiconfig/includes/cortexr5-${machine}-${1}-distro.conf"
    yocto_distro="xilinx-standalone"
  fi

  if [ $1 = "fsbl" ]; then
    r5fsbl_mcdepends="mc::${dtb_file%%.dtb}:fsbl-firmware:do_deploy"
    r5fsbl_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"
  fi
  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-r5-imux.dts ${dtb} ${dtb_file} && rm -f lop-r5-imux.dts.dtb)

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx cortexr5-${machine} $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "cortexr5-${machine}"
DEFAULTTUNE = "cortexr5f"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "$yocto_distro"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

cortex_r5_freertos() {
  if [ $1 = "None" ]; then
    dtb_file="cortexr5-${machine}-freertos.dtb"
    multiconf="${multiconf} cortexr5-${machine}-freertos"
    conf_file="multiconfig/cortexr5-${machine}-freertos.conf"
    libxil="multiconfig/includes/cortexr5-${machine}-libxil.conf"
    distro="multiconfig/includes/cortexr5-${machine}-distro.conf"
  else
    dtb_file="cortexr5-${machine}-${1}-freertos.dtb"
    multiconf="${multiconf} cortexr5-${machine}-${1}-freertos"
    conf_file="multiconfig/cortexr5-${machine}-${1}-freertos.conf"
    libxil="multiconfig/includes/cortexr5-${machine}-${1}-libxil.conf"
    distro="multiconfig/includes/cortexr5-${machine}-${1}-distro.conf"
  fi

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${lops_dir}/lop-r5-imux.dts ${dtb} ${dtb_file} && rm -f lop-r5-imux.dts.dtb)

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx cortexr5-${machine} $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "cortexr5-${machine}"
DEFAULTTUNE = "cortexr5f"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-freertos"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

# Generate microblaze tunings
microblaze_done=0
# Generate microblaze tunings
microblaze_done=0
process_microblaze() {
  if [ ${microblaze_done} = 0 ]; then
    echo -n "Generating microblaze processor tunes..."
    # Process microblaze
    mkdir -p dtb
    (cd dtb ; lopper.py -f --enhanced -i ${lops_dir}/lop-microblaze-yocto.dts ${dtb} && rm -f lop-microblaze-yocto.dts.dtb) > microblaze.conf
    microblaze_done=1
    echo "...done"
  fi
}

# pmu-microblaze is ALWAYS baremetal, no domain
pmu-microblaze() {
  dtb_file="microblaze-pmu.dtb"
  multiconf="${multiconf} microblaze-pmu"
  conf_file="multiconfig/microblaze-pmu.conf"
  libxil="multiconfig/includes/microblaze-pmu-libxil.conf"
  distro="multiconfig/includes/microblaze-pmu-distro.conf"

  pmu_mcdepends="mc::${dtb_file%%.dtb}:pmu-firmware:do_deploy"
  pmu_firmware_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f ${dtb} ${dtb_file})

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx microblaze-pmu $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "microblaze-pmu"

require conf/microblaze.conf
DEFAULTTUNE = "microblaze"
TUNE_FEATURES_tune-microblaze_forcevariable = "\${TUNE_FEATURES_tune-pmu-microblaze}"

TARGET_CFLAGS += "-DPSU_PMU=1U"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

# pmc-microblaze is ALWAYS baremetal, no domain
pmc-microblaze() {
  dtb_file="microblaze-pmc.dtb"
  multiconf="${multiconf} microblaze-pmc"
  conf_file="multiconfig/microblaze-pmc.conf"
  libxil="multiconfig/includes/microblaze-pmc-libxil.conf"
  distro="multiconfig/includes/microblaze-pmc-distro.conf"

  plm_mcdepends="mc::${dtb_file%%.dtb}:plm-firmware:do_deploy"
  plm_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f ${dtb} ${dtb_file})

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx microblaze-plm $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "microblaze-plm"

require conf/microblaze.conf
DEFAULTTUNE = "microblaze"
TUNE_FEATURES_tune-microblaze_forcevariable = "\${TUNE_FEATURES_tune-pmc-microblaze}"

TARGET_CFLAGS += "-DVERSAL_PLM=1"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

# psm-microblaze is ALWAYS baremetal, no domain
psm-microblaze() {
  dtb_file="microblaze-psm.dtb"
  multiconf="${multiconf} microblaze-psm"
  conf_file="multiconfig/microblaze-psm.conf"
  libxil="multiconfig/includes/microblaze-psm-libxil.conf"
  distro="multiconfig/includes/microblaze-psm-distro.conf"

  psm_mcdepends="mc::${dtb_file%%.dtb}:psm-firmware:do_deploy"
  psm_firmware_deploy_dir="\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}/deploy/images/\${MACHINE}"

  # Build device tree
  mkdir -p dtb
  (cd dtb ; LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f ${dtb} ${dtb_file})

  # Build baremetal multiconfig
  mkdir -p multiconfig/includes
  lopper.py -f ${dtb} -- baremetaldrvlist_xlnx microblaze-psm $embeddedsw
  mv libxil.conf ${libxil}
  mv distro.conf ${distro}
  cat << EOF > ${conf_file}
CONFIG_DTFILE = "\${TOPDIR}/conf/dtb/${dtb_file}"
ESW_MACHINE = "microblaze-psm"

require conf/microblaze.conf
DEFAULTTUNE = "microblaze"
TUNE_FEATURES_tune-microblaze_forcevariable = "\${TUNE_FEATURES_tune-psm-microblaze}"

TARGET_CFLAGS += "-DVERSAL_psm=1"

TMPDIR = "\${BASE_TMPDIR}/tmp-${dtb_file%%.dtb}"

DISTRO = "xilinx-standalone"

LIBXIL_CONFIG = "conf/${libxil}"
require conf/${distro}
EOF
}

parse_cpus() {
  while read cpu domain os_hint ; do
    case ${cpu} in
      \#* | \[WARNING\]:) continue ;;
    esac
    if [ ${domain} != "None" ]; then
      echo "Warning: Domains are not yet supported, the generated DTB may be incorrect. ${domain}"
    fi
    case ${cpu} in
      arm,cortex-a53)
        # We need a base cortex_a53_baremetl for the FSBL
        cortex_a53_baremetal fsbl
        if [ ${os_hint} == "None" ]; then
          echo "cortex-a53 for Linux"
          cortex_a53_linux ${domain}
          echo "cortex-a53 for Baremetal"
          cortex_a53_baremetal ${domain}
          echo "cortex-a53 for Freertos"
          cortex_a53_freertos ${domain}
        else
          case ${os_hint} in
            linux*)
              echo "cortex-a53 for Linux ${domain}"
              cortex_a53_linux ${domain}
              ;;
            baremetal*)
              echo "cortex-a53 for Baremetal ${domain}"
              cortex_a53_baremetal ${domain}
              ;;
            freertos*)
              echo "cortex-a53 for Freertos ${domain}"
              cortex_a53_freertos ${domain}
              ;;
            *)
              echo "Warning: cortex-a53 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_a53_baremetal ${domain}
              ;;
          esac
        fi
        ;;
      arm,cortex-a72)
        if [ ${os_hint} == "None" ]; then
          echo "cortex-a72 for Linux"
          cortex_a72_linux ${domain}
          echo "cortex-a72 for Baremetal"
          cortex_a72_baremetal ${domain}
          echo "cortex-a72 for Freertos"
          cortex_a72_freertos ${domain}
        else
          case ${os_hint} in
            linux*)
              echo "cortex-a72 for Linux ${domain}"
              cortex_a72_linux ${domain}
              ;;
            baremetal*)
              echo "cortex-a72 for Baremetal ${domain}"
              cortex_a72_baremetal ${domain}
              ;;
            freertos*)
              echo "cortex-a72 for Freertos ${domain}"
              cortex_a72_freertos ${domain}
              ;;
            *)
              echo "Warning: cortex-a72 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_a72_baremetal ${domain}
              ;;
          esac
        fi
        ;;
      arm,cortex-r5)
        if [ ${os_hint} == "None" ]; then
            if [ "${machine}" = "zynqmp" ]; then
                # We need a base cortex_r5_baremetal for the FSBL for ZynqMP platform
                cortex_r5_baremetal fsbl
	    fi
            echo "cortex-r5 for Baremetal"
            cortex_r5_baremetal ${domain}
            echo "cortex-r5 for Freertos"
            cortex_r5_freertos ${domain}
	else
          case ${os_hint} in
            baremetal*)
              echo "cortex-r5 for Baremetal ${domain}"
              cortex_r5_baremetal ${domain}
              ;;
            freertos*)
              echo "cortex-r5 for Freertos ${domain}"
              cortex_r5_freertos ${domain}
              ;;
            *)
              echo "Warning: cortex-r5 for unknown OS (${os_hint}), parsing baremetal. ${domain}"
              cortex_r5_baremetal ${domain}
              ;;
          esac
        fi
        ;;
      xlnx,microblaze)
        process_microblaze
        case ${os_hint} in
          None | baremetal*)
            echo "Warning: Microblaze for Baremetal ${domain} not yet implemented"
            ;;
          Linux)
            echo "Warning: Microblaze for Linux ${domain} not yet implemented"
            ;;
          *)
            echo "Warning: Microblaze for unknown OS (${os_hint}), not yet implemented. ${domain}"
            ;;
        esac
        ;;
      pmu-microblaze)
        process_microblaze
        echo "Microblaze ZynqMP pmu"
        pmu-microblaze
        ;;
      pmc-microblaze)
        process_microblaze
        echo "Microblaze Versal pmc"
        pmc-microblaze
        ;;
      psm-microblaze)
        process_microblaze
        echo "Microblaze Versal psm"
        psm-microblaze
        ;;
      *)
        echo "Unknown CPU ${cpu}"
        ;;
    esac
  done < cpu-list.tmp
}

# Generate CPU list
cd ${conf_dir}
mkdir -p dtb
(cd dtb ; lopper.py -f --enhanced -i ${lops_dir}/lop-xilinx-id-cpus.dts ${dtb} /dev/null && rm -f lop-xilinx-id-cpus.dts.dtb) > cpu-list.tmp

if [ -z ${machine} ]; then
  detect_machine
  echo "Autodetected machine ${machine}"
else
  echo "Machine ${machine}"
fi

case ${machine} in
  zynqmp | versal) : ;;
  *)
    echo "ERROR: Unknown machine type ${machine}"
    exit 1
    ;;
esac

echo
echo "Generating configuration..."

parse_cpus

echo "...done"
echo
echo

# Cleanup our temp file
rm cpu-list.tmp

echo "To enable this, add the following to your local.conf:"
echo
echo '# Adjust BASE_TMPDIR if you want to move the tmpdirs elsewhere'
echo 'BASE_TMPDIR = "${TOPDIR}"'
if [ -n "${system_conf}" ]; then
  echo "require ${system_conf}"
fi
echo 'SYSTEM_DTFILE = "'${dtb}'"'
echo 'BBMULTICONFIG += "'${multiconf}'"'
if [ -n "${fsbl_mcdepends}" ]; then
  echo 'FSBL_DEPENDS = ""'
  echo 'FSBL_MCDEPENDS = "'${fsbl_mcdepends}'"'
  echo 'FSBL_DEPLOY_DIR = "'${fsbl_deploy_dir}'"'
fi
if [ -n "${r5fsbl_mcdepends}" ]; then
  echo 'R5FSBL_DEPENDS = ""'
  echo 'R5FSBL_MCDEPENDS = "'${r5fsbl_mcdepends}'"'
  echo 'R5FSBL_DEPLOY_DIR = "'${r5fsbl_deploy_dir}'"'
fi
if [ -n "${pmu_mcdepends}" ]; then
  echo 'PMU_DEPENDS = ""'
  echo 'PMU_MCDEPENDS = "'${pmu_mcdepends}'"'
  echo 'PMU_FIRMWARE_DEPLOY_DIR = "'${pmu_firmware_deploy_dir}'"'
fi
if [ -n "${plm_mcdepends}" ]; then
  echo 'PLM_DEPENDS = ""'
  echo 'PLM_MCDEPENDS = "'${plm_mcdepends}'"'
  echo 'PLM_DEPLOY_DIR = "'${plm_deploy_dir}'"'
fi
if [ -n "${psm_mcdepends}" ]; then
  echo 'PSM_DEPENDS = ""'
  echo 'PSM_MCDEPENDS = "'${psm_mcdepends}'"'
  echo 'PSM_FIRMWARE_DEPLOY_DIR = "'${psm_firmware_deploy_dir}'"'
fi
if [ "${machine}" = "versal" ]; then
  echo 'PDI_PATH = "__PATH TO PDI FILE HERE__"'
fi
echo
