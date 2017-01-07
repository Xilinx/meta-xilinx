
# This version extension should match CONFIG_LOCALVERSION in defconfig
XILINX_RELEASE_VERSION ?= ""
LINUX_VERSION_EXTENSION ?= "-xilinx-${XILINX_RELEASE_VERSION}"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
KBRANCH ?= ""
SRCBRANCHARG = "${@['nobranch=1', 'branch=${KBRANCH}'][d.getVar('KBRANCH', True) != '']}"

FILESOVERRIDES_append = ":${LINUX_VERSION}"
SRC_URI = " \
		git://github.com/Xilinx/linux-xlnx.git;protocol=https;${SRCBRANCHARG} \
		"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-machines.inc

KBUILD_DEFCONFIG_zcu102-zynqmp = "xilinx_zynqmp_defconfig"
KCONFIG_MODE_zcu102-zynqmp = "alldefconfig"

