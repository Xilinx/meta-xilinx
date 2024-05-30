BRANCH ?= "xlnx-rebase-v1.22.11"
REPO   ?= "git://github.com/Xilinx/gstreamer.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

PV = "1.22.11+git"

# We need a common PRSERV_PV_AUTOINC for all variations of gstreamer packages
# Need to inject a custom setting, so based on package.bbclass version
PACKAGE_ARCH:task-packagedata = "all"

# However, if we change PACKAGE_ARCH in a task, it changes the WORKDIR and breaks things
# restore the WORKDIR's arch directory (MULTIMACH_TARGET_SYS) to the previous setting!
python() {
  # Anonymous python runs before the task override is evaluated, so set the
  # MULTIMACH_TARGET_SYS (used by WORKDIR)
  d.setVar('MULTIMACH_TARGET_SYS', d.getVar('MULTIMACH_TARGET_SYS'))
}

# Cause all of the variants to rebuild now that we have a common PV
PE = '1'

SRC_URI = " \
    ${REPO};${BRANCHARG};name=gstreamer-xlnx \
"

SRCREV_gstreamer-xlnx = "35b7316e6d323a46668967d54e68a9036e6cd9c2"
SRCREV_FORMAT = "gstreamer-xlnx"
