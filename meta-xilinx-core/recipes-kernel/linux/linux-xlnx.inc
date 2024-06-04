# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"
PV = "${LINUX_VERSION}+git"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
KBRANCH ?= "undefined"
SRCBRANCHARG = "${@['nobranch=1', 'branch=${KBRANCH}'][d.getVar('KBRANCH', True) != '']}"

FILESOVERRIDES:append := ":${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}"
KERNELURI ?= "git://github.com/Xilinx/linux-xlnx.git;protocol=https;name=machine"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=yocto-kmeta"
SRC_URI = "${KERNELURI};${SRCBRANCHARG} ${YOCTO_META}"
SRC_URI += "file://linux-xlnx-kmeta;type=kmeta;name=linux-xlnx-kmeta;destsuffix=linux-xlnx-kmeta"

SRCREV_machine ?= "${SRCREV}"
SRCREV_meta ?= "eeb5d0c9dd5e2928835c633644426ee357fbce12"
SRCREV_FORMAT = "machine"

require recipes-kernel/linux/linux-yocto.inc

LINUX_MB_INC = ""
LINUX_MB_INC:microblaze = "linux-microblaze.inc"
require ${LINUX_MB_INC}

DESCRIPTION = "Xilinx Kernel"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

EXTKERNELSRC = "${@'1' if d.getVar('EXTERNALSRC') else ''}"

# Force the use of the KBUILD_DEFCONFIG even if some other defconfig was generated in the ${WORKDIR}
do_kernel_metadata:prepend () {
	[ -n "${KBUILD_DEFCONFIG}" ] && [ -e ${WORKDIR}/defconfig ] && rm ${WORKDIR}/defconfig
}

do_configure:prepend () {
	if [ -n "${KBUILD_DEFCONFIG}" ] && [ -n "${EXTKERNELSRC}" ]; then
		cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
	fi
}

inherit kernel-simpleimage

# Default to be only compatible with specific machines or soc families
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:microblaze = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

# Use DEFCONFIGs for configuring linux-xlnx kernels
KCONFIG_MODE ?= "alldefconfig"
KBUILD_DEFCONFIG:zynqmp ?= "xilinx_defconfig"
KBUILD_DEFCONFIG:zynq ?= "xilinx_zynq_defconfig"
KBUILD_DEFCONFIG:microblaze ?= "mmu_defconfig"
KBUILD_DEFCONFIG:versal ?= "xilinx_defconfig"
KBUILD_DEFCONFIG:versal-net ?= "xilinx_defconfig"

KERNEL_FEATURES:append:zynqmp = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)}"

KERNEL_FEATURES:append:zynqmp = "${@' features/xilinx/overlay_of/overlay_of.scc' if d.getVar('FPGA_MNGR_RECONFIG_ENABLE') == '1' else ''}"

KERNEL_FEATURES:append:versal = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)} features/xilinx/hdmi-module/hdmi-module.scc"
KERNEL_FEATURES:append:versal-net = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)} features/xilinx/hdmi-module/hdmi-module.scc"

KERNEL_FEATURES:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', ' features/ocicontainer/ocicontainer.scc', '', d)}"
