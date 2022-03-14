REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH= "2022.1"
SRCREV= "445d6db8407b8375b9b870befa84db2f234a25e8"
PV = "202210.2.13.0"



