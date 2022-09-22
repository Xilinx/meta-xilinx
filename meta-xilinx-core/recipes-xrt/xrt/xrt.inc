REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.2"
SRCREV= "a09430a4d1a02de24f3672c78dbda4bd7b790ae0"
PV = "202220.2.14.0"

