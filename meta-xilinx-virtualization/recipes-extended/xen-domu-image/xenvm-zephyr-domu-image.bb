SUMMARY = "Xen Zephyr DomU Guest OS image recipe for ZynqMP"
DESCRIPTION = "Xen DomU guest image recipe that boots a Zephyr RTOS \
payload as an unprivileged guest on AMD ZynqMP Xen virtualization \
stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# kept in the standard EDF artifact shape for the automated updater
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/10310614/external/zephyr-synchronization/xenvm_zephyr-synchronization_10310614.tar.gz"
SRC_URI[sha256sum] = "0ad34d612c4bbd0bc453515a35d4a3084316a7e89ef1fd41304e7a2aee1b7fce"

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
