DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "versal"

# Available SOC_VARIANT's for versal:
# "prime"    - Versal deafult Prime Devices
# "premium"  - Versal Premium Devices
# "hbm"      - Versal HMB Devices
# "ai-core"  - Versal AI-core Devices
# "ai-edge"  - Versal AI-Edge Devices
# "net"      - Versal Net Devices

SOC_VARIANT ?= "prime"

require xilinx-soc-family.inc
require soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

WIC_DEPENDS ?= "virtual/kernel virtual/bootloader virtual/boot-bin virtual/arm-trusted-firmware"

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
