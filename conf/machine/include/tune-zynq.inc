
MACHINE_FEATURES := ""

require conf/machine/include/tune-cortexa9.inc

PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.8%"

KERNEL_IMAGETYPE = "uImage"

IMAGE_FSTYPES = "cpio ext2"

PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"

PREFERRED_VERSION_u-boot-xlnx ?= "v2013.01%"
