BRANCH ?= "xlnx_rel_v2024.2"
REPO   ?= "git://github.com/Xilinx/multimedia-notebooks.git;protocol=https"
SRCREV ?= "ef7a7236144a04977cb5bb800d6d7cf319e52b58"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

PV .= "+git"
