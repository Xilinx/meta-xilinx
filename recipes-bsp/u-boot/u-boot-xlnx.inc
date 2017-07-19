require u-boot.inc

DEPENDS += "dtc-native"

UBOOTURI ?= "git://gitenterprise.xilinx.com/Linux/u-boot-xlnx.git;protocol=https"
UBRANCH ?= "2017.3_uspea_web"

UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH', True) != '']}"
SRC_URI = "${UBOOTURI};${UBRANCHARG}"

S = "${WORKDIR}/git"

