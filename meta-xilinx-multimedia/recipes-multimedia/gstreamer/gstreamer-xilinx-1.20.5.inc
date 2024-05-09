BRANCH ?= "xlnx-rebase-v1.20.5"
REPO   ?= "git://github.com/Xilinx/gstreamer.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

PV = "1.20.5+git"

# We need a common PRSERV_PV_AUTOINC for all variations of gstreamer packages
# Need to inject a custom setting, so based on package.bbclass version
PACKAGE_ARCH:task-packagedata = "all"

# However, if we change PACKAGE_ARCH in a task, it changes the UNPACKDIR and breaks things
# restore the UNPACKDIR's arch directory (MULTIMACH_TARGET_SYS) to the previous setting!
python() {
  # Anonymous python runs before the task override is evaluated, so set the
  # MULTIMACH_TARGET_SYS (used by UNPACKDIR)
  d.setVar('MULTIMACH_TARGET_SYS', d.getVar('MULTIMACH_TARGET_SYS'))
}

# Cause all of the variants to rebuild now that we have a common PV
PE = '1'

SRC_URI = " \
    ${REPO};${BRANCHARG};name=gstreamer-xlnx \
"

SRCREV_gstreamer-xlnx = "5126254d77401b9673602ddcb617ff9e958dd295"
SRCREV_FORMAT = "gstreamer-xlnx"
