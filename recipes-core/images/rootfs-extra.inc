# File containing rootfs related tasks

# Create links to match Xilinx uboot default names
# Do this silently as they may not exist depending on configuration

# When booting with sd card, Xilinx u-boot by default expects
# the rootfs as a u-boot-mkimage wrapped file called uramdisk.image.gz,
# so for convenience symlink the ext2.gz.u-boot file.
# Confirm if all bsps need this?
SRC_MKIMAGE_ROOTFS ?= "${IMAGE_NAME}.rootfs.ext2.gz.u-boot"
SYMLINK_URAMDISK ?= "uramdisk.image.gz"
do_rootfs_append() {
    cd ${DEPLOY_DIR_IMAGE}
    rm -f ${SYMLINK_URAMDISK}
    ln -sf ${SRC_MKIMAGE_ROOTFS} ${SYMLINK_URAMDISK}
}
