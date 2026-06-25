DEFAULTTUNE ?= "microblaze-v"
SOC_FAMILY ?= "microblaze-v"

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
ARCH_DEFAULT_KERNELIMAGETYPE:microblaze-v = "Image"
KEEPUIMAGE = "no"

UBOOT_ELF ?= "u-boot"
