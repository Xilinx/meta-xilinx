SUMMARY = "Xilinx BSP device trees"
DESCRIPTION = "Xilinx BSP device trees from within layer."
SECTION = "bsp"

# the device trees from within the layer are licensed as MIT, kernel includes are GPL
LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = " \
		file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
		file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c \
		"

inherit devicetree image-artifact-names

#this way of going through SRC_URI is better but if dts is including other dtsis, need to add all of them to SRC_URI..
#SRC_URI += "file://${SYSTEM_DTFILE}"
#DT_FILES_PATH = "${@d.getVar('WORKDIR')+'/'+os.path.dirname(d.getVar('SYSTEM_DTFILE'))}"

DT_FILES_PATH = "${@os.path.dirname(d.getVar('SYSTEM_DTFILE')) if d.getVar('SYSTEM_DTFILE') else d.getVar('S')}"

COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"

# Device-trees are inherently board specific
BOARD_ARCH ??= "${MACHINE_ARCH}"
PACKAGE_ARCH = "${BOARD_ARCH}"

DEPENDS += "python3-dtc-native"

PROVIDES = "virtual/dtb"

# common zynq include
SRC_URI:append:zynq = " file://zynq-7000-qspi-dummy.dtsi"

DTB_FILE_NAME = "${@os.path.basename(d.getVar('SYSTEM_DTFILE')).replace('.dts', '.dtb') if d.getVar('SYSTEM_DTFILE') else ''}"
DTB_BASE_NAME ?= "${MACHINE}-system${IMAGE_VERSION_SUFFIX}"

devicetree_do_deploy:append() {
    if [ -n "${DTB_FILE_NAME}" ]; then
        if [ -e "${DEPLOYDIR}/devicetree/${DTB_FILE_NAME}" ]; then
            # We need the output to be system.dtb for WIC setup to match XSCT flow
            ln -sf devicetree/${DTB_FILE_NAME} ${DEPLOYDIR}/${DTB_BASE_NAME}.dtb
            ln -sf devicetree/${DTB_FILE_NAME} ${DEPLOYDIR}/${MACHINE}-system.dtb
            ln -sf devicetree/${DTB_FILE_NAME} ${DEPLOYDIR}/system.dtb
        else
            bberror "Expected filename ${DTB_FILE_NAME} doesn't exist in ${DEPLOYDIR}/devicetree"
        fi
    fi
}
