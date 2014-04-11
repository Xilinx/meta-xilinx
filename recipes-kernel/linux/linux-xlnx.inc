DESCRIPTION = "Xilinx Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCREV}"

# Sources
KBRANCH ?= "master"
SRC_URI = "git://github.com/Xilinx/linux-xlnx.git;protocol=https;branch=${KBRANCH}"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

# Source Directory
S = "${WORKDIR}/git"

# Inherit/include base functionality
inherit kernel
require linux-machine-common.inc
require linux-machine-kconfig.inc

# Override COMPATIBLE_MACHINE to include your machine in a bbappend file.
COMPATIBLE_MACHINE = "qemumicroblaze|qemuzynq|microblaze|zynq"
