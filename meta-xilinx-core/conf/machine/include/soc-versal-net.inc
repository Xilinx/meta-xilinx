DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "versal-net"

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

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

PSM_DEPENDS ??= ""
PSM_MCDEPENDS ??= ""
PSM_FIRMWARE_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PSM_FIRMWARE_IMAGE_NAME ??= "psm-firmware-${MACHINE}"
PSM_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
