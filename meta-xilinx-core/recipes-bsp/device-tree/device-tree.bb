SUMMARY = "Xilinx BSP device trees"
DESCRIPTION = "Xilinx BSP device trees from within layer."
SECTION = "bsp"

# the device trees from within the layer are licensed as MIT, kernel includes are GPL
LICENSE = "MIT & GPL-2.0-or-later"
LIC_FILES_CHKSUM = " \
		file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
		file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c \
		"

inherit devicetree image-artifact-names

# Fall back to SYSTEM_DTFILE if specified...
# CONFIG_DTFILE is intended to hold a specific configuration's (multiconfig)
# device tree, while SYSTEM_DTFILE is used for the full heterogenous
# system.
SYSTEM_DTFILE ??= ""
CONFIG_DTFILE ??= "${SYSTEM_DTFILE}"
DT_FILES_PATH = "${@os.path.dirname(d.getVar('CONFIG_DTFILE')) if d.getVar('CONFIG_DTFILE') else d.getVar('S')}"

COMPATIBLE_MACHINE:zynq   = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"

DEPENDS += "python3-dtc-native"

PROVIDES = "virtual/dtb"

# common zynq include
SRC_URI:append:zynq = " file://zynq-7000-qspi-dummy.dtsi"

DTB_FILE_NAME = "${@os.path.basename(d.getVar('CONFIG_DTFILE')).replace('.dts', '.dtb') if d.getVar('CONFIG_DTFILE') else ''}"

DTB_BASE_NAME ?= "${MACHINE}-system${IMAGE_VERSION_SUFFIX}"

FILES:${PN} += "/boot/system.dtb"
devicetree_do_install:append() {
    if [ -n "${DTB_FILE_NAME}" ]; then
        # If it's already a dtb, we have to copy from the original location
        if [ -e "${DT_FILES_PATH}/${DTB_FILE_NAME}" ]; then
            install -Dm 0644 ${DT_FILES_PATH}/${DTB_FILE_NAME} ${D}/boot/devicetree/${DTB_FILE_NAME}
        fi
        if [ -e "${D}/boot/devicetree/${DTB_FILE_NAME}" ]; then
            # We need the output to be system.dtb for WIC setup to match XSCT flow
            ln -sf devicetree/${DTB_FILE_NAME} ${D}/boot/system.dtb
        else
            bberror "Expected filename ${DTB_FILE_NAME} doesn't exist in ${DEPLOYDIR}/devicetree"
        fi
    fi
}

devicetree_do_deploy:append() {
    if [ -n "${DTB_FILE_NAME}" ]; then
        # If it's already a dtb, we have to copy from the original location
        if [ -e "${DT_FILES_PATH}/${DTB_FILE_NAME}" ]; then
            install -Dm 0644 ${DT_FILES_PATH}/${DTB_FILE_NAME} ${DEPLOYDIR}/devicetree/${DTB_FILE_NAME}
        fi
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

def check_devicetree_variables(d):
    # Don't cache this, as the items on disk can change!
    d.setVar('BB_DONT_CACHE', '1')

    if not d.getVar('CONFIG_DTFILE'):
        raise bb.parse.SkipRecipe("CONFIG_DTFILE or SYSTEM_DTFILE is not defined.")
    else:
        if not os.path.exists(d.getVar('CONFIG_DTFILE')):
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("The device tree %s is not available." % d.getVar('CONFIG_DTFILE'))
        else:
            d.appendVar('SRC_URI', ' file://${CONFIG_DTFILE}')
            d.setVarFlag('do_install', 'file-checksums', '${CONFIG_DTFILE}:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${CONFIG_DTFILE}:True')

python() {
    # Need to allow bbappends to change the check
    check_devicetree_variables(d)
}
