# This has a cortex-a78, but for compatibility set the same as versal
DEFAULTTUNE ?= "cortexa72-cortexa53"

SOC_FAMILY ?= "versal-2ve-2vm"

require conf/machine/include/soc-family.inc
require conf/machine/include/soc-tune-include.inc

# Linux Configuration
ARCH_DEFAULT_KERNELIMAGETYPE:versal-2ve-2vm = "Image"

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

# versal 2ve 2vm (AM026 Table: Primary Boot Interfaces Table):
# 1: QSPI24
# 2: QSPI32
# 3: SD v3.0 first
# 5: SD v2.0
# 6: eMMC (v5.1)
# 8: OSPI
# 11: UFS
# 14: SD v3.0 second
#
# Note: JTAG is defined as 0, but this is not a primary boot mode
#
# If the user does not want to use any of the AVAIL_HW_BOOT_MODES then
# DEFAULT_HW_BOOT_MODE should be blank (or unset)
#
AVAIL_HW_BOOT_MODES = "1 2 3 5 6 8 11 14"

# Verbose mapping for debug
SOC_BOOT_MODE_MAPPING = "1:QSPI24 2:QSPI32 3:SD0_v3.0 5:SD0_v2.0 6:eMMC_v5.1 8:OSPI 11:UFS 14:SD1_v3.0"

# The boot modes that require a boot.bin on the disk
# Note: the disk must be msdos (or gpt-hybrid) partitioned
#       partition 1 must be FAT16/32 (or vFAT)
#       boot.bin file named "boot.bin" or "bootXXXX.bin" where XXXX is 8191 or less.
SOC_DISK_BOOT_MODE_MAPPING = "3 5 6 11 14"
