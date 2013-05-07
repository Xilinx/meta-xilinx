require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

# move this to common include?
XILINX_EXTENSION = "-xlnx"

FILESEXTRAPATHS += "${THISDIR}/u-boot-xlnx:"
SRC_URI = " \
		git://github.com/Xilinx/u-boot-xlnx.git;protocol=git \
		file://microblaze_bootm_Add_support_for_loading_initrd.patch \
		file://microblaze_bootm_Fix_coding_style_issues.patch \
		file://microblaze_Fix_coding_style_for_bootb.patch \
		"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

include u-boot-extra.inc
