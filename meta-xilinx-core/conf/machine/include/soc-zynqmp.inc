DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "zynqmp"

# Available MACHINE_FETURES for zynqmp processors:
# zynqmp-cg - (none)
# zynqmp-eg - mali400
# zynqmp-ev - mali400, vcu
# zynqmp-dr - rfsoc

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

# Support multilib on zynqmp
DEFAULTTUNE:virtclass-multilib-lib32 ?= "armv7vethf-neon-vfpv4"

UBOOT_SUFFIX ?= "bin"

XSERVER_EXT:zynqmp ?= "xf86-video-armsoc"

# Default PMU ROM
PMU_ROM ?= "${DEPLOY_DIR_IMAGE}/pmu-rom.elf"


# Default expects the user to provide a specific pmu-firwmare in the deploy
# directory, named "pmu-firmware-${MACHINE}.elf" and "pmu-firmware-${MACHINE}.bin"
PMU_DEPENDS ??= ""
PMU_MCDEPENDS ??= ""
PMU_FIRMWARE_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-${MACHINE}"

PMU_FIRMWARE_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
