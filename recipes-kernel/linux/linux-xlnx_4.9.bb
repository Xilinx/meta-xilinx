LINUX_VERSION = "4.9"
XILINX_RELEASE_VERSION = "v2017.1"
KBRANCH = "master"
SRCREV ?= "b11f1d3fcb6195933acc39b8d26891847a5ea24d"
KERNELURI = "git://gitenterprise.xilinx.com/Linux/linux-xlnx.git;protocol=https"
include linux-xlnx.inc
