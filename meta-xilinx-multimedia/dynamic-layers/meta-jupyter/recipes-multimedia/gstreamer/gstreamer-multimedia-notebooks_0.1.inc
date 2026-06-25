BRANCH ?= "xlnx_rel_v2026.1"
REPO   ?= "git://github.com/Xilinx/multimedia-notebooks.git;protocol=https"
SRCREV ?= "4e7491e53818a4eed7f5e2753e0b1e5d99bd54ac"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

PV .= "+git"
