require recipes-security/optee/optee-examples.inc
BRANCH ?= "xlnx_rebase_v4.9.0"
REPO ?= "git://github.com/Xilinx/optee_examples.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

SRCREV = "c69678a0097c1f900125aedb27b9a3cd19d5a018"
