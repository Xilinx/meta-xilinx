DEFAULTTUNE ?= "cortexa9thf-neon"
SOC_FAMILY ?= "zynq"

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "uImage"
KERNEL_IMAGETYPES += "zImage"

# WIC Specific dependencies
WIC_DEPENDS ?= "virtual/kernel virtual/bootloader virtual/boot-bin"

UBOOT_ELF ?= "u-boot.elf"

# Default, if multiconfig is off, the fsbl is in the regular deploydir, otherwise
# it is located under a multiconfig specific deploydir
FSBL_DEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'fsbl-fw', '', 'fsbl-firmware:do_deploy', d)}"
FSBL_MCDEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'fsbl-fw', 'mc::fsbl-fw:fsbl-firmware:do_deploy', '', d)}"
FSBL_DEPLOY_DIR ?= "${@bb.utils.contains('BBMULTICONFIG', 'fsbl-fw', '${TOPDIR}/tmp-fsbl-fw/deploy/images/${MACHINE}', '${DEPLOY_DIR_IMAGE}', d)}"
FSBL_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
FSBL_IMAGE_NAME ?= "fsbl-${MACHINE}"
