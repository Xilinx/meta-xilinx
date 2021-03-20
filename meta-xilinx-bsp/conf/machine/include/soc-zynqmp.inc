DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "zynqmp"

# Available SOC_VARIANT's for zynqmp:
# "cg" - Zynq UltraScale+  CG Devices
# "eg" - Zynq UltraScale+  EG Devices
# "ev" - Zynq UltraScale+  EV Devices
# "dr" - Zynq UltraScale+  DR Devices

SOC_VARIANT ?= "eg"

MACHINEOVERRIDES_prepend_zynqmp-eg = "mali400:"
MACHINEOVERRIDES_prepend_zynqmp-ev = "mali400:vcu:"

require soc-tune-include.inc
require xilinx-soc-family.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

# Support multilib on zynqmp
DEFAULTTUNE_virtclass-multilib-lib32 ?= "armv7vethf-neon-vfpv4"

WIC_DEPENDS ?= "virtual/kernel virtual/bootloader virtual/boot-bin virtual/arm-trusted-firmware"

UBOOT_SUFFIX ?= "bin"

XSERVER_EXT_zynqmp ?= "xf86-video-armsoc"

# Default PMU ROM
PMU_ROM ?= "${DEPLOY_DIR_IMAGE}/pmu-rom.elf"

# Default, if multiconfig is off, call pmu-firmware directly, otherwise call the zynqmp-pmufw multiconfig version
# The Linux compatible pmu-firmware though requires meta-xilinx-tools
PMU_DEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'zynqmp-pmufw', '', 'pmu-firmware:do_deploy', d)}"
PMU_MCDEPENDS ?= "${@bb.utils.contains('BBMULTICONFIG', 'zynqmp-pmufw', 'mc::zynqmp-pmufw:pmu-firmware:do_deploy', '', d)}"
PMU_FIRMWARE_DEPLOY_DIR ?= "${@bb.utils.contains('BBMULTICONFIG', 'zynqmp-pmufw', '${TOPDIR}/tmp-microblaze-zynqmp-pmufw/deploy/images/${MACHINE}', '${DEPLOY_DIR_IMAGE}', d)}"
PMU_FIRMWARE_IMAGE_NAME ?= "${@bb.utils.contains('BBMULTICONFIG', 'zynqmp-pmufw', 'pmu-firmware-zynqmp-pmu', 'pmu-firmware-${MACHINE}', d)}"

