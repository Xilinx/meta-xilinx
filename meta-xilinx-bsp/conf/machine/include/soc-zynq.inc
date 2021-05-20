DEFAULTTUNE ?= "cortexa9thf-neon"
SOC_FAMILY ?= "zynq"

# Available SOC_VARIANT's for zynq:
# 7zs - Zynq-7000 Single A9 Core
# 7z  - Zynq-7000 Dual A9 Core

SOC_VARIANT ?= "7z"

require xilinx-soc-family.inc
require soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "uImage"
KERNEL_IMAGETYPES += "zImage"

# Set default load address.
# Override with KERNEL_EXTRA_ARGS_<board> += "..." in machine file if required
KERNEL_EXTRA_ARGS_zynq += "UIMAGE_LOADADDR=0x8000"

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
