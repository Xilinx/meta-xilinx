DEFAULTTUNE ?= "aarch64"
SOC_FAMILY ?= "zynqmp"

require conf/machine/include/arm/arch-armv8.inc
require conf/machine/include/soc-family.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "Image"

# Support multilib on zynqmp
DEFAULTTUNE_virtclass-multilib-lib32 ?= "armv7vethf-neon-vfpv4"
