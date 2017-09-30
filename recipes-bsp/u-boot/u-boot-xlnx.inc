require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native"

UBRANCH ?= "master"
SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https;branch=${UBRANCH}"

S = "${WORKDIR}/git"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot:"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
FILESEXTRAPATHS_prepend := "${@'${THISDIR}/u-boot-xlnx/${XILINX_RELEASE_VERSION}:' if d.getVar('XILINX_RELEASE_VERSION') else ''}"
