REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.2"
SRCREV= "5da182a12e8f8d6e81d7e78186ce85c5f207578e"
PV = "202220.2.14.0"

