COMPATIBLE_MACHINE_cortexa53 = ".*"


REPO = "git://gitenterprise.xilinx.com/Linux/device-tree-xlnx;protocol=https"
BRANCH = "decoupling"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

# SRCREV = "0d68f68f0fc55f08c499589273eeb6d9c71ec285"
SRCREV = "${AUTOREV}"


XSCTH_PROC_cortexa53 ??= "psu_cortexa53_0"
