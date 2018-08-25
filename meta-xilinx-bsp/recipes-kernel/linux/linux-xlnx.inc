# This version extension should match CONFIG_LOCALVERSION in defconfig
XILINX_RELEASE_VERSION ?= ""
LINUX_VERSION_EXTENSION ?= "-xilinx-${XILINX_RELEASE_VERSION}"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Sources, by default allow for the use of SRCREV pointing to orphaned tags/commits
KBRANCH ?= ""
SRCBRANCHARG = "${@['nobranch=1', 'branch=${KBRANCH}'][d.getVar('KBRANCH', True) != '']}"

FILESOVERRIDES_append = ":${XILINX_RELEASE_VERSION}"
KERNELURI ??= "git://github.com/Xilinx/linux-xlnx.git;protocol=https"
SRC_URI = "${KERNELURI};${SRCBRANCHARG}"

SRCREV_machine ??= "${SRCREV}"

require recipes-kernel/linux/linux-yocto.inc
require linux-microblaze.inc

DESCRIPTION = "Xilinx Kernel"

# Force the use of the KBUILD_DEFCONFIG even if some other defconfig was generated in the ${WORKDIR}
do_kernel_metadata_prepend () {
	[ -n "${KBUILD_DEFCONFIG}" ] && [ -e ${WORKDIR}/defconfig ] && rm ${WORKDIR}/defconfig
}

python __anonymous () {
   kerneltypes = d.getVar('KERNEL_IMAGETYPES') or ""
   if 'simpleImage.mb' in kerneltypes.split():
      providerdtb = d.getVar("PREFERRED_PROVIDER_virtual/dtb") or ""
      if providerdtb:
         d.appendVarFlag('do_compile', 'depends', ' virtual/dtb:do_populate_sysroot')
      else:
         bb.error("For MB dts/dtb provider needs to be set")
}

do_compile_prepend_microblaze () {
	if (echo "${KERNEL_IMAGETYPES}" | grep -wq "simpleImage.mb"); then
		install -d ${B}/arch/microblaze/boot/dts
		cp ${RECIPE_SYSROOT}/boot/devicetree/*.dts ${B}/arch/microblaze/boot/dts/mb.dts
	fi
}


# Default to be only compatible with specific machines or soc families
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE_zynq = ".*"
COMPATIBLE_MACHINE_zynqmp = ".*"
COMPATIBLE_MACHINE_microblaze = ".*"

# Use DEFCONFIGs for configuring linux-xlnx kernels
KCONFIG_MODE ?= "alldefconfig"
KBUILD_DEFCONFIG_zynqmp = "xilinx_zynqmp_defconfig"
KBUILD_DEFCONFIG_zynq = "xilinx_zynq_defconfig"
KBUILD_DEFCONFIG_microblaze = "mmu_defconfig"

do_configure_prepend() {
	if [ -n "${KBUILD_DEFCONFIG}" ]; then
		cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
	fi
}

# Add meta-xilinx kmeta, used for MicroBlaze BSP fragments
FILESEXTRAPATHS_prepend := "${THISDIR}:"
SRC_URI_append = " file://xilinx-kmeta;type=kmeta;name=xilinx-kmeta;destsuffix=xilinx-kmeta"

# MicroBlaze BSP fragments
KERNEL_FEATURES_append_kc705-microblazeel = " bsp/kc705-microblazeel/kc705-microblazeel.scc"

KERNEL_FEATURES_append_zynqmp = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' bsp/xilinx/xen.scc', '', d)}"

KERNEL_FEATURES_append_zynqmp = "${@bb.utils.contains('IMAGE_FEATURES', 'fpga-manager', ' features/overlay/overlay.scc', '', d)}"
