
SRCREV = "1160fbcc619f23bf87cde01ed651566474e17eb9"
PV = "v2015.04${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
SRC_URI += " \
		file://zynq-Add-Zynq-PicoZed-board-support.patch \
		file://microblaze-Fix-EMAC-Lite-initialization.patch \
		file://microblaze-generic_defconfig-Disable-configs.patch \
		file://0001-microblaze-Fix-style-issues-in-header-files.patch \
		file://0002-microblaze-Fix-C99-gnu99-compatiblity-for-inline-fun.patch \
		"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

