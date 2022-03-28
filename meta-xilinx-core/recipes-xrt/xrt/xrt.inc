REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.1"
SRCREV= "63ebdf0148634b7fa2880e48276da4ef0dc7ecdf"
PV = "202210.2.13.0"



