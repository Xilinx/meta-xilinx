DESCRIPTION = "Anti-rollback counter for boot firmware"
SUMMARY = "Generates a 4-byte little-endian binary counter embedded in the PDI via BIF optional data (id=0x22)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "${MACHINE}"
COMPATIBLE_MACHINE:versal-net = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm = "${MACHINE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
ROLLBACK_COUNTER_FILE = "bootbin-rollback-counter.bin"

inherit deploy image-artifact-names shared-manifest

IMAGE_NAME_SUFFIX = ""
MANIFEST_COMPONENT_NAME = "bootbin-rollback-counter"
MANIFEST_COMPONENT_FIELDS = "rollback_counter"
MANIFEST_COMPONENT_FIELD_rollback_counter = "${BOOTBIN_ROLLBACK_COUNTER}"

python do_configure() {
    counter = d.getVar('BOOTBIN_ROLLBACK_COUNTER')
    if not counter:
        bb.fatal("BOOTBIN_ROLLBACK_COUNTER is not set")
    try:
        value = int(counter)
    except ValueError:
        bb.fatal("BOOTBIN_ROLLBACK_COUNTER must be an integer, got: %s" % counter)

    with open(d.expand("${B}/${ROLLBACK_COUNTER_FILE}"), 'wb') as f:
        f.write(value.to_bytes(4, 'little'))
}

do_install() {
    install -d ${D}/boot
    install -m 0644 ${B}/${ROLLBACK_COUNTER_FILE} ${D}/boot/
}

do_deploy() {
    install -m 0644 ${B}/${ROLLBACK_COUNTER_FILE} ${DEPLOYDIR}/${IMAGE_NAME}.bin
    ln -s ${IMAGE_NAME}.bin ${DEPLOYDIR}/${IMAGE_LINK_NAME}.bin
}

addtask deploy after do_compile

SYSROOT_DIRS += "/boot"
FILES:${PN} += "/boot/${ROLLBACK_COUNTER_FILE}"
