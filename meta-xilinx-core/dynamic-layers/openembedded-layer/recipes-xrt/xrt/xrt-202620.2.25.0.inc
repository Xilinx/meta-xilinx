REPO ?= "gitsm://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH = "2026.2"
SRCREV_xrt = "745e2d62df2f0bf7e64fc9b3898eeee3225dd84d"