
# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources
KBRANCH ?= "master"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI = " \
		git://github.com/Xilinx/linux-xlnx.git;protocol=https;branch=${KBRANCH} \
		file://xilinx-base;type=kmeta;destsuffix=xilinx-base \
		"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

