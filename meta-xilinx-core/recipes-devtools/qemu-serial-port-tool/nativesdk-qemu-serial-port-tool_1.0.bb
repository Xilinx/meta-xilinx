SUMMARY = "helper script to help configure QEMU serial ports"
DESCRIPTION = "SDK helper that will list QEMU serial ports from hardware \
DTB files and correlate them with domain specific DTBs in order to \
help configure QEMU."
LICENSE = "MIT"
RDEPENDS:${PN} = "nativesdk-python3-core"

LIC_FILES_CHKSUM = "file://${LAYERPATH_xilinx}/scripts/qemu-serial-port-tool;beginline=3;endline=24;md5=4bc9c9676a9bdedf584385e5682bcd1f"

SRC_URI = "file://${LAYERPATH_xilinx}/scripts/qemu-serial-port-tool"

S = "${UNPACKDIR}"

inherit nativesdk

do_compile() {
	:
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${UNPACKDIR}${LAYERPATH_xilinx}/scripts/* ${D}${bindir}/
}
