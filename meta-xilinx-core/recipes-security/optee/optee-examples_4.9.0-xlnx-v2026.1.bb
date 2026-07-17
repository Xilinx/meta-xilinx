require recipes-security/optee/optee-examples.inc
BRANCH ?= "xlnx_rebase_v4.9.0"
REPO ?= "git://github.com/Xilinx/optee_examples.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

SRCREV = "ae26c60834d7633046619d3afdb3a36fe5a92d7b"
