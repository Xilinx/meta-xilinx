REPO ?= "gitsm://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH = "2026.2"
SRCREV_xrt = "4d70d61168c2a6561c2681a42ada673846228213"