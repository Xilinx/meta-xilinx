PV .= "+git"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= ""
UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH', True) != '']}"

SRC_URI = "${UBOOTURI};${UBRANCHARG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

UBOOT_MB_INC = ""
UBOOT_MB_INC:microblaze = "u-boot-xlnx-microblaze.inc"
require ${UBOOT_MB_INC}

