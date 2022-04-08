REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.1"
SRCREV= "accdff0b2b6f59e9ad5954c6fd1f2892004002d1"
PV = "202210.2.13.0"
