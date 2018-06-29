LINUX_VERSION = "4.14"
XILINX_RELEASE_VERSION = "v2018.3"
BRANCH_KERNEL ??= "xlnx_rebase_v4.14"
KBRANCH = "${BRANCH_KERNEL}"
SRCREV ??= "ad4cd988ba86ab0fb306d57f244b7eaa6cce79a4"

include linux-xlnx.inc

