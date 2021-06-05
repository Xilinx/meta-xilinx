REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2021.1"
SRCREV= "0dc9f505a3a910dea96166db7b5df8530b9ae38e"
PV = "202110.2.11.0"



