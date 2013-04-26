
MACHINE_FEATURES := ""

require conf/machine/include/tune-cortexa9.inc

PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.8%"

KERNEL_IMAGETYPE ?= "uImage"

IMAGE_FSTYPES = "cpio ext2 ext2.gz.u-boot ext2.gz"
#IMAGE_FSTYPES = "tar.gz ubi cpio ext2"

# This inherit causes an "ERROR: cannot map 'allarch' to a linux kernel architecture"?
# Issue seems related to inheriting image_types and kernel-arch from image_types_uboot
INHERIT += "image_types_uboot"

PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"

PREFERRED_VERSION_u-boot-xlnx ?= "v2013.01%"
