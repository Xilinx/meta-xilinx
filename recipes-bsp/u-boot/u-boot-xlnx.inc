require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"

XILINX_EXTENSION = "-xilinx"

SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"

SPL_BINARY_zynq = "boot.bin"
UBOOT_SUFFIX = "img"

do_configure_prepend () {
	if test -e ${WORKDIR}/ps7_init.h; then
		cp ${WORKDIR}/ps7_init.h ${S}/board/xilinx/zynq/
		cp ${WORKDIR}/ps7_init.c ${S}/board/xilinx/zynq/
	fi
}

include u-boot-extra.inc

