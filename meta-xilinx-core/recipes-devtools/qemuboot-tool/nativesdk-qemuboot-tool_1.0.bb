SUMMARY = "helper script to adjust and merge multiple qemuboot.conf \
files"
DESCRIPTION = "SDK helper that merges and edits qemuboot.conf \
fragments produced by Yocto so the resulting configuration matches the \
SDK's installation paths."
LICENSE = "MIT"
RDEPENDS:${PN} = "nativesdk-python3-core"

LIC_FILES_CHKSUM = "file://${LAYERPATH_xilinx}/scripts/qemuboot-tool;beginline=5;endline=21;md5=4b89903784b8d154cd8b631388da4f0d"

SRC_URI = "file://${LAYERPATH_xilinx}/scripts/qemuboot-tool"

S = "${UNPACKDIR}"

inherit nativesdk

do_compile() {
	:
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${UNPACKDIR}${LAYERPATH_xilinx}/scripts/* ${D}${bindir}/
}
