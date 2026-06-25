DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "versal"

# Available MACHINE_FETURES for versal processors:
# prime   - (none)
# premium - aie
# hbm     - (none)
# ai-core - aie, vdu
# ai-edge - aie, vdu

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
ARCH_DEFAULT_KERNELIMAGETYPE:versal = "Image"

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

# versal (AM011 See Table: Primary Boot Interfaces Table):
# 1: QSPI24
# 2: QSPI32
# 3: SD0 v3.0
# 5: SD1 (v2.0)
# 6: eMMC1 (v4.51)
# 8: OSPI
# 14: SD1 (v3.0)
#
# Note: JTAG is defined as 0, but this is not a primary boot mode
#
# If the user does not want to use any of the AVAIL_HW_BOOT_MODES then
# DEFAULT_HW_BOOT_MODE should be blank (or unset)
#
AVAIL_HW_BOOT_MODES = "1 2 3 5 6 8 14"

# Verbose mapping for debug
SOC_BOOT_MODE_MAPPING = "1:QSPI24 2:QSPI32 3:SD0_v3.0 5:SD1_v2.0 6:eMMC_v4.51 8:OSPI 14:SD1_v3.0"

# The boot modes that require a boot.bin on the disk
# Note: the disk must be msdos (or gpt-hybrid) partitioned
#       partition 1 must be FAT16/32 (or vFAT)
#       boot.bin file named "boot.bin" or "bootXXXX.bin" where XXXX is 8191 or less.
SOC_DISK_BOOT_MODE_MAPPING = "3 5 6 14"
