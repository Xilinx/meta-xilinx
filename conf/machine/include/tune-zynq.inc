DEFAULTTUNE ?= "cortexa9thf-neon"
SOC_FAMILY ?= "zynq"

require conf/machine/include/tune-cortexa9.inc
require conf/machine/include/soc-family.inc

# Linux Configuration
KERNEL_IMAGETYPE ?= "uImage"

# Set default load address.
# Override with KERNEL_EXTRA_ARGS_<board> += "..." in machine file if required
KERNEL_EXTRA_ARGS_zynq += "UIMAGE_LOADADDR=0x8000"

# KMACHINE
KMACHINE ?= "zynq"
