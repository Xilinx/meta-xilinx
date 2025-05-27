# This has a cortex-a78, but for compatibility set the same as versal
DEFAULTTUNE ?= "cortexa72-cortexa53"

SOC_FAMILY ?= "versal-2ve-2vm"

require conf/machine/include/soc-family.inc
require conf/machine/include/soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

UBOOT_ELF ?= "u-boot.elf"

# Default, if multiconfig is off, call plm/psm-firmware directly, otherwise call the versal-fw multiconfig version
# The Linux compatible plm/psm-firmware though requires meta-xilinx-tools
PLM_DEPENDS ??= ""
PLM_MCDEPENDS ??= ""
PLM_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PLM_IMAGE_NAME ??= "plm-${MACHINE}"
PLM_DEPLOY_DIR[vardepsexclude] += "TOPDIR"

ASU_DEPENDS ??= ""
ASU_MCDEPENDS ??= ""
ASU_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
ASU_IMAGE_NAME ??= "asu-${MACHINE}"
ASU_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
