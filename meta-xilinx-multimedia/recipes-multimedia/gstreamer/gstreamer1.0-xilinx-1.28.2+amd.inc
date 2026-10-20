BRANCH ?= "xlnx-rebase-v1.28.2"
REPO ?= "git://github.com/Xilinx/gstreamer.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

PV .= "+git"

PE = '1'

SRC_URI = " \
    ${REPO};${BRANCHARG};name=gstreamer-xlnx \
"

SRCREV_gstreamer-xlnx = "f7e0608a6c8b40577da6663a72f22fec9867d49f"
SRCREV_FORMAT = "gstreamer-xlnx"
