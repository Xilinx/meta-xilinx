REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2023.1"
SRCREV= "619e4b1238cf68524e4d8f08af0a72277659118a"
PV = "202310.2.15.0"

