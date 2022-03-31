REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "master"
SRCREV= "ac5840899cb350aa0db7575547b49e0fdc24cb97"
PV = "202210.2.13.0"



