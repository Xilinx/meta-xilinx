DESCRIPTION = "Bootbin version file - text format"
SUMMARY = "The BIF file for bootbin requires a version file in a text format"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "${MACHINE}"
COMPATIBLE_MACHINE:versal = "${MACHINE}"
COMPATIBLE_MACHINE:versal-net = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm = "${MACHINE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BOOTBIN_VER_MAIN ??= "${PV}"
BOOTBIN_VERSION_STRING ??= "${MACHINE}-v${BOOTBIN_VER_MAIN}${IMAGE_VERSION_SUFFIX}"
BOOTBIN_VER_FILE ?= "bootbin-version-string.txt"

inherit deploy image-artifact-names

IMAGE_NAME_SUFFIX = ""

python do_configure() {
    with open(d.expand("${B}/${BOOTBIN_VER_FILE}"), "w") as f:
        f.write(d.getVar('BOOTBIN_VERSION_STRING'))
}

do_deploy() {
     install -m 0644 ${B}/${BOOTBIN_VER_FILE} ${DEPLOYDIR}/${IMAGE_NAME}.txt
     ln -s ${IMAGE_NAME}.txt ${DEPLOYDIR}/${IMAGE_LINK_NAME}.txt
}

addtask deploy after do_compile
