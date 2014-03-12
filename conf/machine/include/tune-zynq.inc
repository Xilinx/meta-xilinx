DEFAULTTUNE ?= "zynq"
SOC_FAMILY ?= "zynq"

require conf/machine/include/tune-cortexa9.inc
require conf/machine/include/soc-family.inc

TUNEVALID[zynq] = "Zynq"
AVAILTUNES += "zynq"

TUNE_FEATURES_tune-zynq += "${TUNE_FEATURES_tune-armv7a-neon} zynq"
ARMPKGARCH = "${ARMPKGARCH_tune-armv7a-neon}"
PACKAGE_EXTRA_ARCHS_tune-zynq = "${PACKAGE_EXTRA_ARCHS_tune-armv7a-neon} zynq"

# Linux Configuration
KERNEL_IMAGETYPE ?= "uImage"

# Set default load address.
# Override with KERNEL_EXTRA_ARGS_<board> += "..." in machine file if required
KERNEL_EXTRA_ARGS_zynq += "UIMAGE_LOADADDR=0x8000"

# linux-yocto KMACHINE
KMACHINE ?= "zynq"
