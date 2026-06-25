DEFAULTTUNE ?= "cortexa9thf-neon"
SOC_FAMILY ?= "zynq"

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
ARCH_DEFAULT_KERNELIMAGETYPE:zynq = "uImage"

UBOOT_ELF ?= "u-boot.elf"

# zynq (UG585 See Boot Mode MIO Strapping Pins):
# 1: QSPI
# 2: NOR
# 4: NAND
# 5: SD Card
#
# Note: JTAG is defined as 0, but this is not a primary boot mode
#
# If the user does not want to use any of the AVAIL_HW_BOOT_MODES then
# DEFAULT_HW_BOOT_MODE should be blank (or unset)
#
AVAIL_HW_BOOT_MODES = "1 2 4 5"

# Verbose mapping for debug
SOC_BOOT_MODE_MAPPING = "1:QSPI 2:NOR 3:NAND 5:SD"

# The boot modes that require a boot.bin on the disk
# Note: the disk must be msdos (or gpt-hybrid) partitioned
#       partition 1 must be FAT16/32 (or vFAT)
#       boot.bin file named "boot.bin"
SOC_DISK_BOOT_MODE_MAPPING = "5"
