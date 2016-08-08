# Default Xilinx BSP Machine settings

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# MicroBlaze cannot handle the execution of qemu-linux-user for gobject-introspection-data
MACHINE_FEATURES_BACKFILL_CONSIDERED_append_microblaze = " qemu-usermode"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz.u-boot"
IMAGE_CLASSES += "image_types_uboot"

# Kernel Configuration
XILINX_DEFAULT_KERNEL := "linux-xlnx"

PREFERRED_PROVIDER_virtual/kernel ??= "${XILINX_DEFAULT_KERNEL}"
PREFERRED_VERSION_linux-xlnx ?= "4.6%"

# U-Boot Configuration
XILINX_DEFAULT_UBOOT := "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/bootloader ??= "${XILINX_DEFAULT_UBOOT}"

UBOOT_SUFFIX ?= "img"
UBOOT_SUFFIX_zynqmp ?= "bin"
UBOOT_SUFFIX_microblaze ?= "bin"

# u-boot-xlnx setups up all zynq configs to have embedded device trees (OF_EMBED=y)
UBOOT_OFEMBED ?= ""
UBOOT_OFEMBED_zynq ?= "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/bootloader', 'u-boot-xlnx', '', '-dtb', d)}"

UBOOT_BINARY ?= "u-boot${UBOOT_OFEMBED}.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot"
UBOOT_ELF_aarch64 ?= "u-boot.elf"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS_append_zynqmp = " kernel-module-mali"

XSERVER ?= " \
        xserver-xorg \
        xf86-input-evdev \
        xf86-input-mouse \
        xf86-input-keyboard \
        xf86-video-fbdev \
        "

XSERVER_EXT_zynqmp ?= "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'xserver-xorg-extension-glx', '', d)}"

PREFERRED_VERSION_qemu = "2.2.5"
PREFERRED_VERSION_qemu-native = "2.2.5"
