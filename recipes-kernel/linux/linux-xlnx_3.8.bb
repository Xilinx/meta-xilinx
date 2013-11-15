KBRANCH = "xlnx_3.8"
# See include file for common information
include linux-xlnx.inc

PR = "r1"

# Kernel version and SRCREV correspond to: github.com/Xilinx xlnx_3.8 branch
LINUX_VERSION = "3.8"
SRCREV = "f4ff79d44a966ebea6229213816d17eb472b303e"

SRC_URI_append += "file://libtraceevent-Remove-hard-coded-include-to-usr-local.patch"
