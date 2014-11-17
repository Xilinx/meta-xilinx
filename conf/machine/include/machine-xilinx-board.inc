# Default Xilinx Board settings

EXTRA_IMAGEDEPENDS += "virtual/bootloader"

IMAGE_BOOT_FILES ?= "${KERNEL_IMAGETYPE} u-boot.${UBOOT_SUFFIX}"

