# Define the 'qemu-sd' conversion type
#
# This conversion type pads any image to the 512K boundary to ensure that the
# image file can be used directly with QEMU's SD emulation which requires the
# block device to match that of valid SD card sizes (which are multiples of
# 512K).

CONVERSIONTYPES:append = " qemu-sd qemu-sd-fatimg"
CONVERSION_CMD:qemu-sd () {
	cp ${IMGDEPLOYDIR}/${IMAGE_NAME}.${type} ${IMGDEPLOYDIR}/${IMAGE_NAME}.${type}.qemu-sd
	# Get the wic.qemu-sd file size
	file_size=`stat -c '%s' ${IMGDEPLOYDIR}/${IMAGE_NAME}.${type}.qemu-sd`
	powerof2=1
	file_size=${file_size%.*}
	# Get the next power of 2 value for the image size value
	while [ ${powerof2} -lt ${file_size} ]; do
		powerof2=$(expr $powerof2 \* 2)
	done
	# Resize the image using qemu-img
	qemu-img resize -f raw ${IMGDEPLOYDIR}/${IMAGE_NAME}.${type}.qemu-sd ${powerof2}
}

BOOT_VOLUME_ID ?= "BOOT"
BOOT_SPACE ?= "1047552"
IMAGE_ALIGNMENT ?= "1024"

# Create SD image in case of INITRAMFS_IMAGE set due to circular dependencies.
# This creates FAT partitioned SD image containing boot.bin,boot.scr and rootfs.cpio.gz.u-boot files.
# This is a workaround fix until we fix the circular dependencies
# Usage: IMAGE_FSTYPES:append = " cpio.gz.u-boot.qemu-sd-fatimg"
CONVERSION_CMD:qemu-sd-fatimg () {
	QEMU_IMG="${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type}.qemu-sd-fatimg"
	BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE} + ${IMAGE_ALIGNMENT} - 1)
	BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE_ALIGNED} - ${BOOT_SPACE_ALIGNED} % ${IMAGE_ALIGNMENT})
	QEMUIMG_SIZE=$(expr ${IMAGE_ALIGNMENT} + ${BOOT_SPACE_ALIGNED})
	dd if=/dev/zero of=${QEMU_IMG} bs=1024 count=0 seek=${QEMUIMG_SIZE}
	parted -s ${QEMU_IMG} mklabel msdos
	parted -s ${QEMU_IMG} unit KiB mkpart primary fat32 ${IMAGE_ALIGNMENT} $(expr ${BOOT_SPACE_ALIGNED} \+ ${IMAGE_ALIGNMENT} \- 1)
	parted -s ${QEMU_IMG} set 1 boot on
	parted ${QEMU_IMG} print
	BOOT_BLOCKS=$(LC_ALL=C parted -s ${QEMU_IMG} unit b print | awk '/ 1 / { print substr($4, 1, length($4 -1)) / 512 /2 }')
	rm -f ${WORKDIR}/${BOOT_VOLUME_ID}.img
	mkfs.vfat -n "${BOOT_VOLUME_ID}" -S 512 -C ${WORKDIR}/${BOOT_VOLUME_ID}.img $BOOT_BLOCKS
	if [ -e ${DEPLOY_DIR_IMAGE}/boot.bin ]; then
		mcopy -i ${WORKDIR}/${BOOT_VOLUME_ID}.img -s  ${DEPLOY_DIR_IMAGE}/boot.bin ::/
	fi
	if [ -e ${DEPLOY_DIR_IMAGE}/boot.scr ]; then
		mcopy -i ${WORKDIR}/${BOOT_VOLUME_ID}.img -s  ${DEPLOY_DIR_IMAGE}/boot.scr ::/
	fi
	if [ ${INITRAMFS_IMAGE} = ${IMAGE_BASENAME} ] && [ x"${INITRAMFS_IMAGE_BUNDLE}" != "x1" ]; then
		mcopy -i ${WORKDIR}/${BOOT_VOLUME_ID}.img -s  ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type} ::rootfs.cpio.gz.u-boot
	fi
	dd if=${WORKDIR}/${BOOT_VOLUME_ID}.img of=${QEMU_IMG} conv=notrunc seek=1 bs=$(expr ${IMAGE_ALIGNMENT} \* 1024)
}

CONVERSION_DEPENDS_qemu-sd = "qemu-system-native"
CONVERSION_DEPENDS_qemu-sd-fatimg = "mtools-native:do_populate_sysroot \
				dosfstools-native:do_populate_sysroot \
				parted-native:do_populate_sysroot"
