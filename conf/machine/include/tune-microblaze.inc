DEFAULTTUNE ?= "microblaze"
SOC_FAMILY ?= "microblaze"

require conf/machine/include/tune-microblaze-features.inc

TUNEVALID[microblaze] = "Microblaze"
AVAILTUNES += "microblaze"

TUNE_FEATURES_tune-microblaze = "microblaze"
PACKAGE_EXTRA_ARCHS_tune-microblaze = "${TUNE_PKGARCH}"

# Linux Configuration
PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.8%"
KERNEL_IMAGETYPE ?= "linux.bin"

# File System Configuration
IMAGE_FSTYPES ?= "cpio ext2 ext2.gz.u-boot ext2.gz"
IMAGE_CLASSES += "image_types_uboot"

# U-Boot Configuration
PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"
PREFERRED_VERSION_u-boot-xlnx ?= "v2013.01%"
UBOOT_MACHINE ?= "microblaze-generic"
