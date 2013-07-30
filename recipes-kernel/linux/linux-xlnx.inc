inherit kernel

# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"

require recipes-kernel/linux/linux-yocto.inc
require linux-machine-common.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI = "git://github.com/Xilinx/linux-xlnx;protocol=git;nocheckout=1"

PR = "r1"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCREV}"

# Override COMPATIBLE_MACHINE to include your machine in a bbappend file.
COMPATIBLE_MACHINE = "qemumicroblaze|qemuzynq|microblaze|zynq"

# Common kernel configuration parts
# Arch specific kernel configuration parts
SRC_URI_append_zynq += "file://zynq_defconfig_${LINUX_VERSION}.cfg"
SRC_URI_append_microblaze += "file://microblaze_defconfig_${LINUX_VERSION}.cfg"

# Add the modules directory to the 'kernel-base' files list
FILES_kernel-base_append = " /lib/modules/${KERNEL_VERSION}/kernel"
