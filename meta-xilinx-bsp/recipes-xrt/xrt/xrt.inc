REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2021.2"
SRCREV= "b783bb51d39dda95aae8a06eef0f432e97bc9c99"
PV = "202120.2.12.0"



