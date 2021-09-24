REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2021.2"
SRCREV= "1d45978df26cd62c2974d34689a816a1aa2613bf"
PV = "202120.2.12.0"



