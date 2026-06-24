REPO ?= "gitsm://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH = "master"
SRCREV_xrt = "0026185de81a179dc7d886197d3e35f9a179b4e8"