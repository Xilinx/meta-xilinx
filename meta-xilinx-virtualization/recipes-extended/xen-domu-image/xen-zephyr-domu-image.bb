SUMMARY = "Xen Zephyr DomU Guest OS image recipe"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05040513/external/zephyr-synchronization/xenvmgicv3_zephyr-synchronization_05040513.tar.gz;name=tarball \
    file://zephyr-synchronization-xenvmgicv3.cfg \
    "
SRC_URI[tarball.sha256sum] = "74e05ca8b9485f32c949ae74896fd60f8aa6ec1a290ba132c46b4ef39f13be7c"

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
    install -m 0644 ${WORKDIR}/xenvmgicv3_zephyr-synchronization/zephyr-synchronization-xenvmgicv3.bin ${D}/boot
    install -d -m 0755 ${D}${sysconfdir}/xen
    install -m 0644 ${WORKDIR}/zephyr-synchronization-xenvmgicv3.cfg ${D}${sysconfdir}/xen/zephyr-synchronization-xenvmgicv3.cfg

}

FILES:${PN} += " \
    /boot/* \
    ${sysconfdir}/xen/zephyr-synchronization-xenvmgicv3.cfg \
    "
