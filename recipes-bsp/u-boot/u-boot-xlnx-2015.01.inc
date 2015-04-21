
SRCREV = "58359ed54834e42e5f9ee336a7daa62c3c86c32a"
PV = "v2015.01${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
SRC_URI += " \
		file://zynq-Add-Zynq-PicoZed-board-support.patch \
		file://microblaze-Fix-EMAC-Lite-initialization.patch \
		file://microblaze-generic_defconfig-Disable-configs.patch \
		"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

