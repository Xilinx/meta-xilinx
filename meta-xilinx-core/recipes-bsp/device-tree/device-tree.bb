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
#SRC_URI += "file://${CONFIG_DTFILE}"
#DT_FILES_PATH = "${@d.getVar('WORKDIR')+'/'+os.path.dirname(d.getVar('CONFIG_DTFILE'))}"

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

do_install:prepend() {
    for DTB_FILE in ${CONFIG_DTFILE}; do
        install -Dm 0644 ${DTB_FILE} ${D}/boot/devicetree/$(basename ${DTB_FILE})
    done
}

devicetree_do_deploy:append() {
    for DTB_FILE in ${CONFIG_DTFILE}; do
        install -Dm 0644 ${DTB_FILE} ${DEPLOYDIR}/devicetree/$(basename ${DTB_FILE})
    done

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

def check_devicetree_variables(d):
    if not d.getVar('CONFIG_DTFILE'):
        d.setVar('BB_DONT_CACHE', '1')
        raise bb.parse.SkipRecipe("CONFIG_DTFILE or SYSTEM_DTFILE is not defined.")
    else:
        if not os.path.exists(d.getVar('CONFIG_DTFILE')):
            d.setVar('BB_DONT_CACHE', '1')
            raise bb.parse.SkipRecipe("The device tree %s is not available." % d.getVar('CONFIG_DTFILE'))
        else:
            d.appendVar('SRC_URI', ' file://${CONFIG_DTFILE}')
            d.setVarFlag('do_install', 'file-checksums', '${CONFIG_DTFILE}:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${CONFIG_DTFILE}:True')

python() {
    # Need to allow bbappends to change the check
    check_devicetree_variables(d)
}
