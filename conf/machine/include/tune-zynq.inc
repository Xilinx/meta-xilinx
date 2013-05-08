DEFAULTTUNE ?= "zynq"
SOC_FAMILY ?= "zynq"

require conf/machine/include/tune-cortexa9.inc

TUNEVALID[zynq] = "Zynq"
AVAILTUNES += "zynq"

TUNE_FEATURES_tune-zynq += "${TUNE_FEATURES_tune-armv7a-neon} zynq"
ARMPKGARCH = "${ARMPKGARCH_tune-armv7a-neon}"
PACKAGE_EXTRA_ARCHS_tune-zynq = "${PACKAGE_EXTRA_ARCHS_tune-armv7a-neon} zynq"

# Linux Configuration
PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.8%"
KERNEL_IMAGETYPE ?= "uImage"

# File System Configuration
IMAGE_FSTYPES = "cpio ext2 ext2.gz.u-boot ext2.gz"
IMAGE_CLASSES += "image_types_uboot"

# U-Boot Configuration
PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"
PREFERRED_VERSION_u-boot-xlnx ?= "v2013.01%"
