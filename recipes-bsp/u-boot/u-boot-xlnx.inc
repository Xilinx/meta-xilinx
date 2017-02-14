require u-boot.inc

DEPENDS += "dtc-native"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= "master"

UBRANCHARG = "${@['nobranch=1', 'branch=${KBRANCH}'][d.getVar('UBRANCH', True) != '']}"
SRC_URI = "${UBOOTURI};branch=${UBRANCH}"

S = "${WORKDIR}/git"

