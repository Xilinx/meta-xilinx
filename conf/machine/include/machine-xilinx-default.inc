# Default Xilinx BSP Machine settings

# File System Configuration
IMAGE_FSTYPES ?= "cpio tar.gz ext2 ext2.gz.u-boot cpio.gz.u-boot"
IMAGE_CLASSES += "image_types_uboot"

# Kernel Configuration
PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.14%"
PREFERRED_VERSION_linux-yocto ?= "3.14%"

# U-Boot Configuration
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx"
PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"
PREFERRED_VERSION_u-boot-xlnx ?= "v2014.01%"

# Architecture specific kernel configuration parts, defconfigs for Xilinx architectures
MACHINE_KCONFIG_prepend += "common/linux/${SOC_FAMILY}/defconfig_${@'.'.join(d.getVar('LINUX_VERSION', True).split('.')[0:2]).strip('+')}.cfg "

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "device-tree"

# Common Device Tree Includes
MACHINE_DEVICETREE_append_zynq += " \
		common/zynq7-base.dtsi \
		"
