REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2023.1"
SRCREV= "f0c2e397211347dfa1ea92910712d481fe43f662"
PV = "202310.2.15.0"

