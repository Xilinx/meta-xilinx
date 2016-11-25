LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
SRCBRANCH ?= ""
SRCBRANCHARG = "${@['nobranch=1', 'branch=${SRCBRANCH}'][d.getVar('SRCBRANCH', True) != '']}"

FILESEXTRAPATHS_prepend := "${THISDIR}/config:"
SRC_URI = " \
		git://github.com/Xilinx/linux-xlnx.git;protocol=https;${SRCBRANCHARG} \
		"

SRC_URI_append_zynqmp += "${@base_contains('DISTRO_FEATURES', 'xen', ' file://zynqmp/xen/xen.scc', '', d)}"
SRC_URI_append_kc705-microblazeel += "file://kc705-microblazeel/kc705-microblazeel.scc"
SRC_URI_append_qemumicroblaze += "file://qemumicroblaze/qemumicroblaze.scc"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

SRCTREECOVEREDTASKS = "do_kernel_configme do_validate_branches do_kernel_configcheck do_kernel_checkout do_fetch do_unpack do_patch"

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-machines.inc

KBUILD_DEFCONFIG_zynqmp = "xilinx_zynqmp_defconfig"
KBUILD_DEFCONFIG_zynq = "xilinx_zynq_defconfig"
KBUILD_DEFCONFIG_microblaze = "mmu_defconfig"
KCONFIG_MODE = "--alldefconfig"

do_configure_prepend() {
	if [ -n "${KBUILD_DEFCONFIG}" ]; then
		cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
	fi
}
