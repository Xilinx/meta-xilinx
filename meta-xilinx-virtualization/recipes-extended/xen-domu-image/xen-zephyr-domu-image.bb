SUMMARY = "Xen Zephyr DomU Guest OS image recipe"
DESCRIPTION = "Xen DomU guest image recipe that boots a Zephyr RTOS \
payload as an unprivileged guest on AMD Xilinx Xen virtualization \
stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# guest board per SoC: versal is GIC-v3, zynqmp is GIC-v2
XEN_ZEPHYR_DOMU_MACHINE = "xenvmgicv3"
XEN_ZEPHYR_DOMU_MACHINE:zynqmp = "xenvm"

XEN_ZEPHYR_DOMU_NAME = "zephyr-synchronization-${XEN_ZEPHYR_DOMU_MACHINE}"
XEN_ZEPHYR_DOMU_DIR = "${XEN_ZEPHYR_DOMU_MACHINE}_zephyr-synchronization"

SRC_URI = "\
    https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08230755/external/zephyr-synchronization/xenvmgicv3_zephyr-synchronization_08230755.tar.gz;name=xenvmgicv3 \
    file://zephyr-synchronization-xenvmgicv3.cfg \
    "

SRC_URI:zynqmp = "\
    https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/zephyr-synchronization/xenvm_zephyr-synchronization_08221309.tar.gz;name=xenvm \
    file://zephyr-synchronization-xenvm.cfg \
    "

SRC_URI[xenvmgicv3.sha256sum] = "336c5d86f5262a7cd03729a2b40e6cf32edfc53ca85414d7ea2fd110f8a651c8"
SRC_URI[xenvm.sha256sum] = "2d89c451b27b927c779f46123bc175c2aea38cd92d1a9fb2cc83b91b9b4a6dae"

# any machine or soc specific override requires a different package_arch
PACKAGE_ARCH:zynqmp = "${MACHINE_ARCH}"
PACKAGE_ARCH:versal = "${MACHINE_ARCH}"
PACKAGE_ARCH:versal-2ve-2vm = "${MACHINE_ARCH}"
PACKAGE_ARCH:versal-2vp = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "${MACHINE}"
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
    install -m 0644 ${UNPACKDIR}/${XEN_ZEPHYR_DOMU_DIR}/${XEN_ZEPHYR_DOMU_NAME}.bin ${D}/boot
    install -d -m 0755 ${D}${sysconfdir}/xen
    install -m 0644 ${UNPACKDIR}/${XEN_ZEPHYR_DOMU_NAME}.cfg ${D}${sysconfdir}/xen/${XEN_ZEPHYR_DOMU_NAME}.cfg

}

FILES:${PN} += " \
    /boot/* \
    ${sysconfdir}/xen/${XEN_ZEPHYR_DOMU_NAME}.cfg \
    "

CONFFILES:${PN} += "${sysconfdir}/xen/${XEN_ZEPHYR_DOMU_NAME}.cfg"
