DEFAULTTUNE ?= "cortexa72-cortexa53"
SOC_FAMILY ?= "versal"

# Available SOC_VARIANT's for versal:
# virt

SOC_VARIANT ?= "s80"

require conf/machine/include/tune-cortexa72-cortexa53.inc
require conf/machine/include/xilinx-soc-family.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

WIC_DEPENDS ?= "virtual/kernel virtual/bootloader virtual/boot-bin virtual/arm-trusted-firmware"

UBOOT_ELF ?= "u-boot.elf"
