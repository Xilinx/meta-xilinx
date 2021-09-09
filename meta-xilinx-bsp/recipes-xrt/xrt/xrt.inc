REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "master"
SRCREV= "b0a62137a1b90a80ee2d273dd3d7f9b0a56ffe5d"
PV = "202120.2.12.0"



