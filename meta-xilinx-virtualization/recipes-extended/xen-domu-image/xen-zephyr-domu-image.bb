SUMMARY = "Xen Zephyr DomU Guest OS image recipe"
DESCRIPTION = "Xen DomU guest image recipe that boots a Zephyr RTOS \
payload as an unprivileged guest on AMD Xilinx Xen virtualization \
stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/06092108/external/zephyr-synchronization/xenvmgicv3_zephyr-synchronization_06092108.tar.gz;name=tarball \
    file://zephyr-synchronization-xenvmgicv3.cfg \
    "
SRC_URI[tarball.sha256sum] = "eb783b170b8e583a01c3858ee2165ba85090a7d81feed435798f9f80bddd2d63"

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
