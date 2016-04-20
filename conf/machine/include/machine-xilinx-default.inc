# Default Xilinx BSP Machine settings

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz.u-boot"
IMAGE_CLASSES += "image_types_uboot"

# Kernel Configuration
XILINX_DEFAULT_KERNEL := "linux-xlnx"

KBUILD_DEFCONFIG_zynq = "xilinx_zynq_defconfig"
KBUILD_DEFCONFIG_zynqmp = "xilinx_zynqmp_defconfig"
KBUILD_DEFCONFIG_microblaze = "mmu_defconfig"

KCONFIG_MODE = "--alldefconfig"

PREFERRED_PROVIDER_virtual/kernel ??= "${XILINX_DEFAULT_KERNEL}"
PREFERRED_VERSION_linux-xlnx ?= "4.4%"

# U-Boot Configuration
XILINX_DEFAULT_UBOOT := "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/bootloader ??= "${XILINX_DEFAULT_UBOOT}"

UBOOT_SUFFIX ?= "img"
UBOOT_SUFFIX_zynqmp ?= "bin"
UBOOT_SUFFIX_microblaze ?= "bin"
UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot"
UBOOT_ELF_aarch64 ?= "u-boot.elf"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS_zynqmp = "kernel-modules"

XSERVER = " \
           xserver-xorg \
           xf86-input-evdev \
           xf86-input-mouse \
           xf86-input-keyboard \
           ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'xserver-xorg-extension-glx', '', d)} \
           "

