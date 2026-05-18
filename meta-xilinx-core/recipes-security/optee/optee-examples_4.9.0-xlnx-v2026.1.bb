require recipes-security/optee/optee-examples.inc
BRANCH ?= "xlnx_rebase_v4.9.0"
REPO ?= "git://github.com/Xilinx/optee_examples.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

SRCREV = "e6e5152e21fd23b5a913027611bfaec69e30b455"
