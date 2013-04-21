require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

# move this to common include?
XILINX_EXTENSION = "-xlnx"

SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=git \
"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

include u-boot-extra.inc
