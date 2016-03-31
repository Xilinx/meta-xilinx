SUMMARY = "Xilinx Platform Headers"
DESCRPTION = "Xilinx ps7_init_gpl.c/h platform headers, used for building u-boot-spl and fsbl"
HOMEPAGE = "http://www.xilinx.com"
SECTION = "bsp"

include zynq7-platform-init.inc

COMPATIBLE_MACHINE = "$^"
COMPATIBLE_MACHINE_picozed-zynq7 = "picozed-zynq7"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "files://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://ps7_init_gpl.c file://ps7_init_gpl.h"

S = "${WORKDIR}"

do_install_append() {
	install -m 0644 ${S}/ps7_init_gpl.c ${D}${PLATFORM_INIT_DIR}/
	install -m 0644 ${S}/ps7_init_gpl.h ${D}${PLATFORM_INIT_DIR}/
}

