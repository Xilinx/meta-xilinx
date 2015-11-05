require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "dtc-native"

XILINX_EXTENSION = "-xilinx"

UBRANCH ?= "master"
SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https;branch=${UBRANCH}"

S = "${WORKDIR}/git"
PACKAGE_ARCH = "${MACHINE_ARCH}"

