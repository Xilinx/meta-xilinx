BRANCH ?= "xlnx-rebase-v1.28.2"
REPO ?= "git://github.com/Xilinx/gstreamer.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

PV .= "+git"

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

SRCREV_gstreamer-xlnx = "5209e704b286620d1874ffbfa87aa66c78ed807f"
SRCREV_FORMAT = "gstreamer-xlnx"
