
# This version extension should match CONFIG_LOCALVERSION in defconfig
XILINX_RELEASE_VERSION ?= ""
LINUX_VERSION_EXTENSION ?= "-xilinx-${XILINX_RELEASE_VERSION}"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
KBRANCH ?= ""
SRCBRANCHARG = "${@['nobranch=1', 'branch=${KBRANCH}'][d.getVar('KBRANCH', True) != '']}"

FILESOVERRIDES_append = ":${LINUX_VERSION}"
KERNELURI ?= "git://github.com/Xilinx/linux-xlnx.git;protocol=https"
SRC_URI = " \
		${KERNELURI};${SRCBRANCHARG} \
		file://xilinx-base;type=kmeta;name=kmeta-xilinx-base;destsuffix=xilinx-base \
		"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

KBUILD_DEFCONFIG_zcu102-zynqmp = "xilinx_zynqmp_defconfig"
KCONFIG_MODE_zcu102-zynqmp = "alldefconfig"

KERNEL_FEATURES_append = " bsp/xilinx/soc/linux-xlnx/drivers/xilinx.scc"
KERNEL_FEATURES_append_zynq = " bsp/xilinx/soc/linux-xlnx/drivers/zynq7.scc"
KERNEL_FEATURES_append_zynqmp = " \
		bsp/xilinx/soc/linux-xlnx/drivers/zynqmp.scc \
		features/drm/drm-xilinx.scc \
		features/v4l2/v4l2-xilinx.scc \
		"

