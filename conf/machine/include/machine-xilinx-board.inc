# Default Xilinx Board settings

EXTRA_IMAGEDEPENDS += "virtual/bootloader"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS_append_microblaze += "device-tree"

IMAGE_BOOT_FILES ?= "${KERNEL_IMAGETYPE} ${UBOOT_BINARY}"

