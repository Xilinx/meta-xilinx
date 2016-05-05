
# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
SRCBRANCH ?= ""
SRCBRANCHARG = "${@['nobranch=1', 'branch=${SRCBRANCH}'][d.getVar('SRCBRANCH', True) != '']}"

FILESEXTRAPATHS_prepend := "${THISDIR}/config:"
SRC_URI = " \
		git://github.com/Xilinx/linux-xlnx.git;protocol=https;${SRCBRANCHARG} \
		file://kc705;type=kmeta;destsuffix=kc705 \
		file://zynqmp;type=kmeta;destsuffix=zynqmp \
		"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-machines.inc

KERNEL_FEATURES_append_zynqmp = " zynqmp.scc"
