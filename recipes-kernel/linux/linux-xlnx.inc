inherit kernel

# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"

require recipes-kernel/linux/linux-yocto.inc

# MicroBlaze is a uImage target, but its not called 'uImage'
DEPENDS_append_microblaze += "u-boot-mkimage-native"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI = "git://github.com/Xilinx/linux-xlnx;protocol=git;nocheckout=1"

# QEMU MicroBlaze Machine Configurations
SRC_URI_append_qemumicroblaze += " \
		file://qemumicroblaze.cfg \
		file://qemumicroblaze.dts \
		"
KERNEL_DEVICETREE_qemumicroblaze = "${WORKDIR}/qemumicroblaze.dts"
SRC_URI_append_qemuzynq += " \
		file://qemuzynq.cfg \
		file://qemuzynq.dts \
		"
KERNEL_DEVICETREE_qemuzynq = "${WORKDIR}/qemuzynq.dts"
#KERNEL_DEVICETREE_qemuzynq = "${S}/arch/arm/boot/dts/zynq-zc702.dts"

PR = "r1"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"

# Override COMPATIBLE_MACHINE to include your machine in a bbappend file.
COMPATIBLE_MACHINE = "qemumicroblaze|qemuzynq"
