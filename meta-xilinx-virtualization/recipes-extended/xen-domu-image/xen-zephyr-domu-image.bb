SUMMARY = "Xen Zephyr DomU Guest OS image recipe"
DESCRIPTION = "Xen DomU guest image recipe that boots a Zephyr RTOS \
payload as an unprivileged guest on AMD Xilinx Xen virtualization \
stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07231200/external/zephyr-synchronization/xenvmgicv3_zephyr-synchronization_07231200.tar.gz;name=tarball \
    file://zephyr-synchronization-xenvmgicv3.cfg \
    "
SRC_URI[tarball.sha256sum] = "9ec6020c2ef72e31bd77ed01b58eec2813ae3660ba7275e089246e36cd8057db"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm = "${MACHINE}"

do_configure() {
	:
}

do_compile() {
	:
}

do_install () {
    install -d ${D}/boot
    install -m 0644 ${UNPACKDIR}/xenvmgicv3_zephyr-synchronization/zephyr-synchronization-xenvmgicv3.bin ${D}/boot
    install -d -m 0755 ${D}${sysconfdir}/xen
    install -m 0644 ${UNPACKDIR}/zephyr-synchronization-xenvmgicv3.cfg ${D}${sysconfdir}/xen/zephyr-synchronization-xenvmgicv3.cfg

}

FILES:${PN} += " \
    /boot/* \
    ${sysconfdir}/xen/zephyr-synchronization-xenvmgicv3.cfg \
    "
