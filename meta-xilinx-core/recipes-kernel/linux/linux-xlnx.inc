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

SRC_URI:append:versal-2ve-2vm = "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://linux-optee.cfg', '', d)}\
    "

# MicroBlaze-V (MicroBlaze RISC-V) include file.
LINUX_MBV_INC = ""
LINUX_MBV_INC:microblaze-v = "linux-mbv.inc"
require ${LINUX_MBV_INC}

SRC_URI_RISCV = "\
    file://risc-v-isa-clear.cfg \
    ${@bb.utils.contains(    "TUNE_FEATURES", "rv 32 i m a", "file://risc-v-isa-rv32i.cfg", "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "rv 64 i m a", "file://risc-v-isa-rv64i.cfg", "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "f d",         "file://risc-v-isa-fpu.cfg",   "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "c",           "file://risc-v-isa-c.cfg",     "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "v",           "file://risc-v-isa-v.cfg",     "", d)} \
    ${@bb.utils.contains_any("TUNE_FEATURES", "b zbb",       "file://risc-v-isa-zbb.cfg",   "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "zicbom",      "file://risc-v-isa-zicbom.cfg",   "", d)} \
    "

SRC_URI:append:riscv32 = "${SRC_URI_RISCV}"
SRC_URI:append:riscv64 = "${SRC_URI_RISCV}"

SRCREV_machine ?= "${SRCREV}"
SRCREV_meta ?= "eeb5d0c9dd5e2928835c633644426ee357fbce12"
SRCREV_FORMAT = "machine"

# The following inherits kernel.bbclass which overrides the system ??= value
# for KERNEL_IMAGETYPE.  Expand things here as KERNEL_IMAGETYPE is now
# fixed.
KERNEL_IMAGETYPE := "${KERNEL_IMAGETYPE}"

require recipes-kernel/linux/linux-yocto.inc

LINUX_MB_INC = ""
LINUX_MB_INC:microblaze = "linux-microblaze.inc"
require ${LINUX_MB_INC}

SUMMARY = "AMD Xilinx Linux kernel (linux-xlnx)."
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
COMPATIBLE_MACHINE:microblaze = ".*"
COMPATIBLE_MACHINE:riscv32 = ".*"
COMPATIBLE_MACHINE:riscv64 = ".*"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

# Use DEFCONFIGs for configuring linux-xlnx kernels
KCONFIG_MODE ?= "alldefconfig"
KBUILD_DEFCONFIG_DEFAULT ?= ""
KBUILD_DEFCONFIG_DEFAULT:microblaze ?= "mmu_defconfig"
KBUILD_DEFCONFIG_DEFAULT:riscv32 ?= "defconfig"
KBUILD_DEFCONFIG_DEFAULT:riscv64 ?= "defconfig"
KBUILD_DEFCONFIG_DEFAULT:zynq ?= "xilinx_zynq_defconfig"
KBUILD_DEFCONFIG_DEFAULT:zynqmp ?= "xilinx_defconfig"
KBUILD_DEFCONFIG_DEFAULT:versal ?= "xilinx_defconfig"
KBUILD_DEFCONFIG_DEFAULT:versal-net ?= "xilinx_defconfig"
KBUILD_DEFCONFIG_DEFAULT:versal-2ve-2vm ?= "xilinx_defconfig"

KBUILD_DEFCONFIG ?= "${KBUILD_DEFCONFIG_DEFAULT}"

KERNEL_FEATURES:append:zynqmp = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)}"

KERNEL_FEATURES:append:zynqmp = "${@' features/xilinx/overlay_of/overlay_of.scc' if d.getVar('FPGA_MNGR_RECONFIG_ENABLE') == '1' else ''}"

KERNEL_FEATURES:append:versal = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)} features/xilinx/hdmi-module/hdmi-module.scc features/xilinx/hdmi-module/hdmi-module-fix.scc"
KERNEL_FEATURES:append:versal-net = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)} features/xilinx/hdmi-module/hdmi-module.scc features/xilinx/hdmi-module/hdmi-module-fix.scc"
KERNEL_FEATURES:append:versal-2ve-2vm = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' features/xen/xen.scc', '', d)} features/xilinx/hdmi-module/hdmi-module.scc features/xilinx/hdmi-module/hdmi-module-fix.scc"

KERNEL_FEATURES:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', ' features/ocicontainer/ocicontainer.scc', '', d)}"

SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'file://extra-configs.cfg', '', d)}"

# To enable lttng, we need kprobes
KERNEL_FEATURES_KPROBES = " features/kprobes"

# Not supported on Microblaze
KERNEL_FEATURES_KPROBES:microblaze = ""

KERNEL_FEATURES:append = "${KERNEL_FEATURES_KPROBES}"
