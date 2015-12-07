
# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
SRCBRANCH ?= ""
SRCBRANCHARG = "${@['nobranch=1', 'branch=${SRCBRANCH}'][d.getVar('SRCBRANCH', True) != '']}"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI = " \
		git://github.com/Xilinx/linux-xlnx.git;protocol=https;${SRCBRANCHARG} \
		file://xilinx-base;type=kmeta;destsuffix=xilinx-base \
		"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

KERNEL_FEATURES_append = " bsp/xilinx/soc/linux-xlnx/drivers/xilinx.scc"
KERNEL_FEATURES_append_zynq = " bsp/xilinx/soc/linux-xlnx/drivers/zynq7.scc"
KERNEL_FEATURES_append_zynqmp = " bsp/xilinx/soc/linux-xlnx/drivers/zynqmp.scc"

