# Default Xilinx BSP Machine settings

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz.u-boot"
IMAGE_CLASSES += "image_types_uboot"

# Kernel Configuration
XILINX_DEFAULT_KERNEL := "linux-xlnx"
XILINX_DEFAULT_KERNEL_microblaze := "linux-yocto"
XILINX_DEFAULT_KERNEL_zynqmp := "linux-yocto"

PREFERRED_PROVIDER_virtual/kernel ??= "${XILINX_DEFAULT_KERNEL}"
PREFERRED_VERSION_linux-xlnx ?= "3.19%"
PREFERRED_VERSION_linux-yocto ?= "4.1%"

# U-Boot Configuration
XILINX_DEFAULT_UBOOT := "u-boot-xlnx"
XILINX_DEFAULT_UBOOT_zynqmp := "u-boot"
PREFERRED_PROVIDER_virtual/bootloader ??= "${XILINX_DEFAULT_UBOOT}"

UBOOT_SUFFIX ?= "img"
UBOOT_SUFFIX_zynqmp ?= "bin"
UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_BINARY_zynq ?= "u-boot-dtb.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot"
UBOOT_ELF_aarch64 ?= "u-boot.elf"

# By default use the device-tree recipe to generate device trees for Zynq machines
MACHINE_ESSENTIAL_EXTRA_RDEPENDS_append_zynq += "device-tree"

# Common Device Tree Includes
MACHINE_DEVICETREE_append_zynq += " \
		common/zynq7-base.dtsi \
		"

