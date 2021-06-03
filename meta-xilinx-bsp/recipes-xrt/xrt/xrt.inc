REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2021.1"
SRCREV= "ffb3dd98002e8c5b6a5660fb679cfa2106c02697"
PV = "202110.2.11.0"



