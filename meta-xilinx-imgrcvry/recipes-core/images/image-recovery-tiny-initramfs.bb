SUMMARY = "Image capable of booting a image-recovery utility."
DESCRIPTION = "Minimal rootfs image for Image recovery"
LICENSE = "MIT"

inherit core-image

# Do not include boot files in the image
IMAGE_BOOT_FILES = ""

IMAGE_NAME_SUFFIX ?= ""
IMAGE_LINGUAS = ""

# don't actually generate an image, just the artifacts needed for one
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Make sure the corresponding kernel is also built and deployed
EXTRA_IMAGEDEPENDS += "${KERNEL_DEPLOY_DEPEND}"

IMAGE_CLASSES += "image-types-imgrcvry"

IMAGE_TYPEDEP:imagercvry = "cpio.lzma.u-boot"
INITRAMFS_FSTYPES += "${IMAGE_TYPEDEP:imagercvry} imagercvry"

PACKAGE_INSTALL = " \
        packagegroup-core-boot \
        dropbear image-recovery-linux-dev mtd-utils \
        i2c-tools libubootenv-bin ufs-utils \
        bmap-writer bash fwenv-initramfs mac-address-config image-recovery-launcher \
        "
