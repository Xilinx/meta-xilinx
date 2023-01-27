REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "master"
SRCREV= "94bcc5d36e958e3f4b3e5f42c0128a2e6c1b9025"
PV = "202310.2.15.0"

