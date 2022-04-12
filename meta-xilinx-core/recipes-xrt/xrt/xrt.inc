REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.1"
SRCREV= "2a6dc026480914ea1c9f02977a6ab4b57e8a3c8d"
PV = "202210.2.13.0"
