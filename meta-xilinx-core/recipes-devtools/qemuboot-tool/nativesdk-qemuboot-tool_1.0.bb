SUMMARY = "helper script to adjust and merge multiple qemuboot.conf files"
LICENSE = "MIT"
RDEPENDS:${PN} = "nativesdk-python3-core"

LIC_FILES_CHKSUM = "file://${LAYERPATH_xilinx}/scripts/qemuboot-tool;beginline=5;endline=21;md5=4b89903784b8d154cd8b631388da4f0d"

SRC_URI = "file://${LAYERPATH_xilinx}/scripts/qemuboot-tool"

S = "${WORKDIR}"

inherit nativesdk

do_compile() {
	:
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}${LAYERPATH_xilinx}/scripts/* ${D}${bindir}/
}
