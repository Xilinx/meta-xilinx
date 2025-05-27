BRANCH ?= "xlnx_rel_v2025.1"
REPO   ?= "git://github.com/Xilinx/multimedia-notebooks.git;protocol=https"
SRCREV ?= "71b1bfb808b02c21f2845021add72ae7ffaa07ef"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

PV .= "+git"
