DESCRIPTION = "Bootbin version file - text format"
SUMMARY = "The BIF file for bootbin requires a version file in a text format"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BOOTBIN_VER_MAIN ?= ""
BOOTBIN_VER_SUFFIX ?= "${@(d.getVar('XILINX_VER_BUILD') or '')[:8] if d.getVar('XILINX_VER_UPDATE') != 'release' and not d.getVar('XILINX_VER_UPDATE').startswith('update') else ''}"
BOOTBIN_VER_FILE = "bootbin-version-string.txt"

#BOOTBIN_MANIFEST_FILE ?= "bootbin-version-header.manifest"

inherit deploy image-artifact-names

IMAGE_NAME_SUFFIX = ""

python do_configure() {

    if not 'version' in locals():
        version = d.getVar("MACHINE") + "-v" + d.getVar("BOOTBIN_VER_MAIN")
    version += d.getVar("IMAGE_VERSION_SUFFIX")

    with open(d.expand("${B}/${BOOTBIN_VER_FILE}"), "w") as f:
        f.write(version)
}

do_deploy() {
     install -m 0644 ${B}/${BOOTBIN_VER_FILE} ${DEPLOYDIR}/${IMAGE_NAME}.txt
     ln -s ${IMAGE_NAME}.txt ${DEPLOYDIR}/${IMAGE_LINK_NAME}.txt
#     install -m 0644 ${B}/${BOOTBIN_MANIFEST_FILE} ${DEPLOYDIR}/${IMAGE_NAME}.manifest
#     ln -s ${IMAGE_NAME}.manifest ${DEPLOYDIR}/${IMAGE_LINK_NAME}.manifest
}

addtask deploy after do_compile
