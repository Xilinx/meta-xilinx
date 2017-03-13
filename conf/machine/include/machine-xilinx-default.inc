# Default Xilinx BSP Machine settings

include conf/machine/include/machine-xilinx-${SOC_FAMILY}.inc
include conf/machine/include/machine-xilinx-${MACHINE}.inc

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# MicroBlaze cannot handle the execution of qemu-linux-user for gobject-introspection-data
MACHINE_FEATURES_BACKFILL_CONSIDERED_append_microblaze = " qemu-usermode"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz.u-boot cpio.gz"
IMAGE_CLASSES += "image_types_uboot"

# Kernel Configuration
XILINX_DEFAULT_KERNEL ?= "linux-xlnx"
XILINX_DEFAULT_KERNEL_VERSION ?= "4.9%"

PREFERRED_PROVIDER_virtual/kernel ??= "${XILINX_DEFAULT_KERNEL}"
PREFERRED_VERSION_linux-xlnx ?= "${XILINX_DEFAULT_KERNEL_VERSION}"

# U-Boot Configuration
XILINX_DEFAULT_UBOOT ?= "u-boot-xlnx"
XILINX_DEFAULT_UBOOT_VERSION ?= "v2017.01%"
PREFERRED_PROVIDER_virtual/bootloader ??= "${XILINX_DEFAULT_UBOOT}"
PREFERRED_VERSION_u-boot-xlnx ?= "${XILINX_DEFAULT_UBOOT_VERSION}"

PREFERRED_PROVIDER_virtual/mesa_zynqmp = "mesa-gl"
PREFERRED_PROVIDER_virtual/libgl_zynqmp = "mesa-gl"
PREFERRED_PROVIDER_virtual/libgles1_zynqmp = "libmali-xlnx"
PREFERRED_PROVIDER_virtual/libgles2_zynqmp = "libmali-xlnx"
PREFERRED_PROVIDER_virtual/egl_zynqmp = "libmali-xlnx"

UBOOT_SUFFIX ?= "bin"

# u-boot-xlnx setups up all zynq configs to have embedded device trees (OF_EMBED=y)
UBOOT_OFEMBED ?= ""
UBOOT_OFEMBED_zynq ?= "${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/bootloader', 'u-boot-xlnx u-boot-xlnx-dev', '', '-dtb', d)}"

UBOOT_BINARY ?= "u-boot${UBOOT_OFEMBED}.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot"
UBOOT_ELF_aarch64 ?= "u-boot.elf"

XSERVER ?= " \
	xserver-xorg \
	xf86-input-evdev \
	xf86-input-mouse \
	xf86-input-keyboard \
	xf86-video-fbdev \
	${XSERVER_EXT} \
	"

XSERVER_EXT ?= ""
XSERVER_EXT_zynqmp ?= "xf86-video-armsoc"

EXTRA_IMAGEDEPENDS_append_zynqmp += " arm-trusted-firmware"

# Support multilib on zynqmp
DEFAULTTUNE_virtclass-multilib-lib32_zynqmp = "armv7vehf-neon-vfpv4"
