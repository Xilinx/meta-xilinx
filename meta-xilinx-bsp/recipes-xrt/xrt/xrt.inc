REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2021.1"
SRCREV= "af60b85faa5b44772fc0066f491feb2ed181ffb8"
PV = "202110.2.11.0"



