DESCRIPTION = "Bootbin version string file in hex format for boot header"
SUMMARY = "Generates a hex-encoded version string for the Zynq/ZynqMP User Defined Field Boot Header (udf_bh)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynq = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp = "${MACHINE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BOOTBIN_VER_MAIN ??= "${PV}"
BOOTBIN_VERSION_STRING ??= "${MACHINE}-v${BOOTBIN_VER_MAIN}"

BOOTBIN_VER_FILE = "bootbin-version-header.txt"
BOOTBIN_VER_MAX_LEN = "40"
BOOTBIN_VER_MAX_LEN:zynq = "76"

inherit deploy image-artifact-names shared-manifest

IMAGE_NAME_SUFFIX = ""
MANIFEST_COMPONENT_NAME = "bootbin-version-header"
MANIFEST_COMPONENT_FIELDS = "version"
MANIFEST_COMPONENT_FIELD_version = "${BOOTBIN_VERSION_STRING}"

python do_configure() {
    version = d.getVar('BOOTBIN_VERSION_STRING')
    if len(version) > int(d.getVar("BOOTBIN_VER_MAX_LEN")):
        bb.fatal("version string too long (%s) [%d > %d]" % (version, len(version), int(d.getVar("BOOTBIN_VER_MAX_LEN"))))

    with open(d.expand("${B}/${BOOTBIN_VER_FILE}"), 'w') as f:
        f.write(version.encode("utf-8").hex())
}

do_install() {
    install -d ${D}/boot
    install -m 0644 ${B}/${BOOTBIN_VER_FILE} ${D}/boot/
}

do_deploy() {
    install -m 0644 ${B}/${BOOTBIN_VER_FILE} ${DEPLOYDIR}/${IMAGE_NAME}.txt
    ln -s ${IMAGE_NAME}.txt ${DEPLOYDIR}/${IMAGE_LINK_NAME}.txt
}

addtask deploy after do_compile

SYSROOT_DIRS += "/boot"
FILES:${PN} += "/boot/${BOOTBIN_VER_FILE}"
