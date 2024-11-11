DEFAULTTUNE ?= "cortexa9thf-neon"
SOC_FAMILY ?= "zynq"

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "uImage"
KERNEL_IMAGETYPES += "zImage"

UBOOT_ELF ?= "u-boot.elf"
