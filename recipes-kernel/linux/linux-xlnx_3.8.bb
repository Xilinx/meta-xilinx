KBRANCH = "xlnx_3.8"
# See include file for common information
include linux-xlnx.inc

PR = "r1"

# Kernel version and SRCREV correspond to: github.com/Xilinx xlnx_3.8 branch
LINUX_VERSION = "3.8"
#SRCREV = "7a65c6dd165a8fc052ba0321eb706536e6cbef71"
SRCREV ?= "${AUTOREV}"

