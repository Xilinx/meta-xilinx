DESCRIPTION = "Minimal rootfs image for Image recovery"
LICENSE = "MIT"

require recipes-core/images/core-image-tiny-initramfs.bb

IMAGE_CLASSES += "image-types-imgrcvry"

INITRAMFS_FSTYPES += "cpio.gz.u-boot imagercvry"

PACKAGE_INSTALL = "imgrcvry-initramfs-live-boot-tiny packagegroup-core-boot dropbear \
		${VIRTUAL-RUNTIME_base-utils} ${VIRTUAL-RUNTIME_dev_manager} base-passwd \
		image-recovery-linux-dev mtd-utils i2c-tools ufs-utils bmap-writer bash \
		"

