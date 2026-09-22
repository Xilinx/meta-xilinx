SUMMARY = "Xilinx DisplayPort Linux Kernel module"
DESCRIPTION = "Out-of-tree DisplayPort (DP) kernel modules for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PV = "6.18.0+git"


REPO   = "git://github.com/Xilinx/dp-modules.git;protocol=https"
BRANCH = "master"
SRCREV = "deda821e2b0794e9cebd594f0e3826a5141d0767"

SRC_URI = "${REPO};branch=${BRANCH}"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"
COMPATIBLE_MACHINE:versal-2vp = ".*"
