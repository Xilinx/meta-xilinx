REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2023.1"
SRCREV= "67a13ed5a477cfa24a9dd3d58ccb97a910f10dac"
PV = "202310.2.15.0"

