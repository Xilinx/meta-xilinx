# Default Xilinx BSP Machine settings

# File System Configuration
IMAGE_FSTYPES ?= "cpio tar.gz ext2 ext2.gz.u-boot ext2.gz"
IMAGE_CLASSES += "image_types_uboot"

# Kernel Configuration
PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.8%"
PREFERRED_VERSION_linux-yocto ?= "3.8%"
PREFERRED_VERSION_linux-yocto-tiny ?= "3.8%"

# U-Boot Configuration
PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"
PREFERRED_VERSION_u-boot-xlnx ?= "v2013.01%"
