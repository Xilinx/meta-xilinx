SUMMARY = "Xen Zephyr DomU Guest OS image recipe for ZynqMP"
DESCRIPTION = "Xen DomU guest image recipe that boots a Zephyr RTOS \
payload as an unprivileged guest on AMD ZynqMP Xen virtualization \
stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# kept in the standard EDF artifact shape for the automated updater
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/10290558/external/zephyr-synchronization/xenvm_zephyr-synchronization_10290558.tar.gz"
SRC_URI[sha256sum] = "e09a04631c1680170b506d2e7919949849b92d43c04a81f25db2efba8253e62a"

SRC_URI += "file://zephyr-synchronization-xenvm.cfg"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "${MACHINE}"

S = "${UNPACKDIR}"

do_configure() {
	:
}

do_compile() {
	:
}

do_install () {
    install -d ${D}/boot
    install -m 0644 ${UNPACKDIR}/xenvm_zephyr-synchronization/zephyr-synchronization-xenvm.bin ${D}/boot
    install -d -m 0755 ${D}${sysconfdir}/xen
    install -m 0644 ${UNPACKDIR}/zephyr-synchronization-xenvm.cfg ${D}${sysconfdir}/xen/zephyr-synchronization-xenvm.cfg

}

FILES:${PN} += " \
    /boot/* \
    ${sysconfdir}/xen/zephyr-synchronization-xenvm.cfg \
    "

CONFFILES:${PN} += "${sysconfdir}/xen/zephyr-synchronization-xenvm.cfg"
