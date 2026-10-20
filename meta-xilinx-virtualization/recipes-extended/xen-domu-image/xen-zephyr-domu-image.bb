SUMMARY = "Xen Zephyr DomU Guest OS image recipe"
DESCRIPTION = "Xen DomU guest image recipe that boots a Zephyr RTOS \
payload as an unprivileged guest on AMD Xilinx Xen virtualization \
stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/10200613/external/zephyr-synchronization/xenvmgicv3_zephyr-synchronization_10200613.tar.gz;name=tarball \
    file://zephyr-synchronization-xenvmgicv3.cfg \
    "
SRC_URI[tarball.sha256sum] = "2dad769fc29c90ffcef70ffcc290cb60f122eb0663bde012d8e051a2bdfd1f0c"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2vp = "${MACHINE}"

S = "${UNPACKDIR}"

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

CONFFILES:${PN} += "${sysconfdir}/xen/zephyr-synchronization-xenvmgicv3.cfg"
