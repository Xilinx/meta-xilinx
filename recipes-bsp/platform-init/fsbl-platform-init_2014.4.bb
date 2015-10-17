SUMMARY = "Xilinx FSBL Platform Headers"
DESCRPTION = "Xilinx FSBL ps7_init_gpl.c/h platform headers, used for building u-boot-spl and fsbl"
HOMEPAGE = "http://www.xilinx.com"
SECTION = "bsp"

include zynq7-platform-init.inc

COMPATIBLE_MACHINE = "zc702-zynq7|zc706-zynq7|zedboard-zynq7|microzed-zynq7"

# This license refers to only the ps7_init_gpl.c and ps7_init_gpl.h, all other
# files in the repo have a different license, refer to the source for more
# detail.
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "files://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://github.com/Xilinx/embeddedsw.git;protocol=https;nobranch=1"
SRCREV = "5f9ae570bf665fcded71364d19b6fae73e63190b"

S = "${WORKDIR}/git"

BOARD_NAME_zc702-zynq7 = "zc702"
BOARD_NAME_zc706-zynq7 = "zc706"
BOARD_NAME_zedboard-zynq7 = "zed"
BOARD_NAME_microzed-zynq7 = "microzed"

FSBL_MISC_PATH = "lib/sw_apps/zynq_fsbl/misc/${BOARD_NAME}"

do_install_append() {
	install -m 0644 ${S}/${FSBL_MISC_PATH}/ps7_init_gpl.c ${D}${PLATFORM_INIT_DIR}/
	install -m 0644 ${S}/${FSBL_MISC_PATH}/ps7_init_gpl.h ${D}${PLATFORM_INIT_DIR}/
}

