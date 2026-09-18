PV .= "+git"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= ""
UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH') != '']}"

SRC_URI = "${UBOOTURI};${UBRANCHARG}"

B = "${WORKDIR}/build"

