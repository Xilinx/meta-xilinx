REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "master"
SRCREV= "57c655bf98d1c9bc3dc4ba5e3bf7cf80a5e5ecc3"
PV = "202220.2.14.0"



