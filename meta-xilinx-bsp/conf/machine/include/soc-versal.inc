DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "versal"

# Available SOC_VARIANT's for versal:
# "prime"    - Versal deafult Prime Devices
# "ai-core"  - Versal AI-core Devices
# "premium"  - Versal Premium Devices

SOC_VARIANT ?= "prime"

require xilinx-soc-family.inc
require soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

WIC_DEPENDS ?= "virtual/kernel virtual/bootloader virtual/boot-bin virtual/arm-trusted-firmware"

UBOOT_ELF ?= "u-boot.elf"

# Default, if multiconfig is off, call plm/psm-firmware directly, otherwise call the versal-fw multiconfig version
# The Linux compatible plm/psm-firmware though requires meta-xilinx-tools
PLM_DEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', '', 'plm-firmware:do_deploy', d)}"
PLM_MCDEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'mc::versal-fw:plm-firmware:do_deploy', '', d)}"
PLM_DEPLOY_DIR ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', '${TOPDIR}/tmp-microblaze-versal-fw/deploy/images/${MACHINE}', '${DEPLOY_DIR_IMAGE}', d)}"
PLM_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
PLM_IMAGE_NAME ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'plm-versal-mb', 'plm-${MACHINE}', d)}"

PSM_DEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', '', 'psm-firmware:do_deploy', d)}"
PSM_MCDEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'mc::versal-fw:psm-firmware:do_deploy', '', d)}"
PSM_FIRMWARE_DEPLOY_DIR ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', '${TOPDIR}/tmp-microblaze-versal-fw/deploy/images/${MACHINE}', '${DEPLOY_DIR_IMAGE}', d)}"
PSM_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
PSM_FIRMWARE_IMAGE_NAME ?= "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'psm-firmware-versal-mb', 'psm-firmware-${MACHINE}', d)}"
