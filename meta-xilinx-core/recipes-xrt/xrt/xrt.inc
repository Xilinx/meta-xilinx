REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.2"
SRCREV= "43926231f7183688add2dccfd391b36a1f000bea"
PV = "202220.2.14.0"

