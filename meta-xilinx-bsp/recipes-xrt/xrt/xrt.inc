REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "master"
SRCREV= "a511f3697b669a4a5dfbfd22b7aaebcf1bcf630a"
PV = "202120.2.12.0"



