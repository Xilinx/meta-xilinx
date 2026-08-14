REPO ?= "gitsm://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH = "master"
SRCREV_xrt = "e9db9ab15f10173f8d2fc93ff92ab4c7eb09d2e6"