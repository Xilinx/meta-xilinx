#
# Create an fitImage image that contents:
#  - 1 kernel image
#  - 1 rootfs image
#  - 1 dtb image
#  - 2 configuration
#   * conf1: kernel + rootfs + dtb
#   * conf2: kernel + dtb (prepare for sd-root)
#
# Assuming the images are in the ${DEPLOY_DIR_IMAGE} directory
#

FITIMAGE_ROOTFS_COMPRESSION_TYPE ?= "none"
FITIMAGE_ROOTFSIMG ?= ""

FITIMAGE_KERNELIMG ?= ""
FITIMAGE_KERNEL_COMPRESSION_TYPE ?= "none"
FITIMAGE_KERNEL_ENTRYPOINT ?= "0x80000"
FITIMAGE_KERNEL_LOADADDRESS ?= "0x80000"

FITIMAGE_DTBIMG ?= ""

DEPENDS = " \
	u-boot-mkimage-native \
	dtc-native \
	"

# Final fitimage name
FITIMAGE_NAME ?= "fit.itb"

python () {
    # auto set up the kernel image
    fitimg = d.getVar('FITIMAGE_KERNELIMG', True)
    if fitimg is None or fitimg == "":
        kimg_type = d.getVar('KERNEL_IMAGETYPE', True)
        if kimg_type == "zImage" or kimg_type == "Image":
            d.setVar('FITIMAGE_KERNELIMG', "${DEPLOY_DIR_IMAGE}/%s" %kimg_type)
        else:
            raise bb.parse.SkipPackage("Set KERNEL_IMAGETYPE to zImage|Image to enable xilinx-fitimage")

    # auto set up the dtb (first *.dtb found in DEPLOY_DIR_IMAGE)
    fitdtb = d.getVar("FITIMAGE_DTBIMG", True)
    if fitdtb is None or fitdtb == "":
        import glob
        for dtb in glob.glob("%s/*.dtb" %d.getVar("DEPLOY_DIR_IMAGE", True)):
            d.setVar('FITIMAGE_DTBIMG', "${DEPLOY_DIR_IMAGE}/%s" %(os.path.basename(dtb)))
            break
}

addtask do_xilinx_fitimage after do_image_complete before do_build

do_xilinx_fitimage[depends] += "virtual/kernel:do_deploy virtual/dtb:do_deploy"
do_xilinx_fitimage () {
	cd ${B}
	do_assemble_xilinx_fitimage

	echo "Copying fit-image.its source file..."
	fitimg_bn=fitImage.itb-${PV}-${PR}
	its_base_name="fitImage.its-${PV}-${PR}"
	its_symlink_name=fitimage.its
	install -m 0644 fit-image.its ${DEPLOY_DIR_IMAGE}/${its_base_name}
	install -m 0644 fitImage ${DEPLOY_DIR_IMAGE}/${fitimg_bn}

	ln -sf ${fitimg_bn} ${DEPLOY_DIR_IMAGE}/${FITIMAGE_NAME}
	ln -sf ${its_base_name} ${DEPLOY_DIR_IMAGE}/${its_symlink_name}
}

do_assemble_xilinx_fitimage[vardepsexclude] = "DEPLOY_DIR_IMAGE  XILINXBASE FITIMAGE_KERNELIMG FITIMAGE_DTBIMG"
do_assemble_xilinx_fitimage() {
	if [ -z "${FITIMAGE_DTBIMG}" ]; then
		bbfatal "No dtb was defined, Please set FITIMAGE_DTBIMG appropriately."
	fi

	if [ "${FITIMAGE_ROOTFSIMG}" = "" ]; then
		${XILINXBASE}/scripts/bin/mkits.sh -v "${MACHINE}" \
			-k "${FITIMAGE_KERNELIMG}" -c 2 \
			-e "${FITIMAGE_KERNEL_ENTRYPOINT}" \
			-a "${FITIMAGE_KERNEL_LOADADDRESS}" \
			-d "${FITIMAGE_DTBIMG}" -c 2 \
			-o fit-image.its
	else
		${XILINXBASE}/scripts/bin/mkits.sh -v "${MACHINE}" \
			-k "${FITIMAGE_KERNELIMG}" -c 1 -c 2 \
			-C "${FITIMAGE_KERNEL_COMPRESSION_TYPE}" \
			-e "${FITIMAGE_KERNEL_ENTRYPOINT}" \
			-a "${FITIMAGE_KERNEL_LOADADDRESS}" \
			-r "${DEPLOY_DIR_IMAGE}/${FITIMAGE_ROOTFSIMG}" -c 1 \
			-C "${FITIMAGE_ROOTFS_COMPRESSION_TYPE}" \
			-d "${FITIMAGE_DTBIMG}" -c 1 -c 2 \
			-o fit-image.its
	fi
	uboot-mkimage -f fit-image.its fitImage
}
