
SRCREV = "03464615e241054a38cd920980d6b12feba95585"
PV = "v2015.01${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
SRC_URI += " \
		file://zynq-Add-Zynq-PicoZed-board-support.patch \
		"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

