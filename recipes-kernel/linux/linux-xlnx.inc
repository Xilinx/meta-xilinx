
# This version extension should match CONFIG_LOCALVERSION in defconfig
XILINX_RELEASE_VERSION ?= ""
LINUX_VERSION_EXTENSION ?= "-xilinx-${XILINX_RELEASE_VERSION}"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
KBRANCH ?= "2017.3_uspea_web"
SRCBRANCHARG = "${@['nobranch=1', 'branch=${KBRANCH}'][d.getVar('KBRANCH', True) != '']}"

FILESEXTRAPATHS_prepend := "${THISDIR}/config:"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

KERNELURI ?= "git://gitenterprise.xilinx.com/Linux/linux-xlnx.git;protocol=https"
SRC_URI = "${KERNELURI};${SRCBRANCHARG}"

SRC_URI += " \
	${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' file://zynqmp/xen/xen.scc', '', d)} \
	file://zynqmp/sdfec/sdfec.cfg \
	"
SRC_URI_append_kc705-microblazeel = " file://kc705-microblazeel/kc705-microblazeel.scc"

SRCREV_machine ?= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc

DESCRIPTION = "Xilinx Kernel"

require linux-xilinx-machines.inc

KBUILD_DEFCONFIG_zynqmp = "xilinx_zynqmp_defconfig"
KBUILD_DEFCONFIG_zynq = "xilinx_zynq_defconfig"
KBUILD_DEFCONFIG_microblaze = "mmu_defconfig"
KCONFIG_MODE = "alldefconfig"

do_configure_prepend() {
	if [ -n "${KBUILD_DEFCONFIG}" ]; then
		cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
	fi
}
