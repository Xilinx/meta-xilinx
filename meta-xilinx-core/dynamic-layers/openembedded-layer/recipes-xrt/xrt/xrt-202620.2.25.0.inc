REPO ?= "gitsm://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH = "2026.2"
SRCREV_xrt = "10929ffd47bb7cfa651f1cdd8c31299fa919a001"