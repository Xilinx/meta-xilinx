REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2021.2"
SRCREV= "0fedb0d6730445888d247e7053e557a3a1f23fbe"
PV = "202120.2.12.0"



