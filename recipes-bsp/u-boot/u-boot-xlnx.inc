require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

XILINX_EXTENSION = "-xilinx"

FILESEXTRAPATHS += "${THISDIR}/u-boot-xlnx:"
SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"

include u-boot-extra.inc

