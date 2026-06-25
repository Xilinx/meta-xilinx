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
ARCH_DEFAULT_KERNELIMAGETYPE:zynqmp = "Image"

# Support multilib on zynqmp
DEFAULTTUNE:virtclass-multilib-lib32 ?= "armv7vethf-neon-vfpv4"

UBOOT_SUFFIX ?= "bin"

XSERVER_EXT:zynqmp ?= "xf86-video-armsoc"

# Default expects the user to provide a specific pmu-firwmare in the deploy
# directory, named "pmu-firmware-${MACHINE}.elf"
PMU_DEPENDS ??= ""
PMU_MCDEPENDS ??= ""
PMU_FIRMWARE_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-${MACHINE}"

PMU_FIRMWARE_DEPLOY_DIR[vardepsexclude] += "TOPDIR"

# zynqmp (UG1085 See Table 11-1: Boot Modes):
# 1: QSPI (24b)
# 2: QSPI (32b)
# 3: SD0 (2.0)
# 4: NAND
# 5: SD1 (2.0)
# 6: eMMC (1.8V)
# 14: SD1 LS (3.0)
#
# Note: JTAG is defined as 0, but this is not a primary boot mode
#
# If the user does not want to use any of the AVAIL_HW_BOOT_MODES then
# DEFAULT_HW_BOOT_MODE should be blank (or unset)
#
AVAIL_HW_BOOT_MODES = "1 2 3 4 5 6 14"

# Verbose mapping for debug
SOC_BOOT_MODE_MAPPING = "1:QSPI_24b 2:QSPI_32b 3:SD_2.0 4:NAND 5:SD1_2.0 6:eMMC_1.8V 14:SD1_LS_3.0"

# The boot modes that require a boot.bin on the disk
# Note: the disk must be msdos (or gpt-hybrid) partitioned
#       partition 1 must be FAT16/32 (or vFAT)
#       boot.bin file named "boot.bin" or "bootXXXX.bin" where XXXX is 8191 or less.
SOC_DISK_BOOT_MODE_MAPPING = "3 5 6 14"
