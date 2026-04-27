SUMMARY = "Xilinx DisplayPort Linux Kernel module"
DESCRIPTION = "Out-of-tree DisplayPort (DP) kernel modules for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PV = "6.18.0+git"

S = "${WORKDIR}/git"

REPO   = "git://github.com/Xilinx/dp-modules.git;protocol=https"
BRANCH = "master"
SRCREV = "20b98d2b13d249ee3da68c417bbf35539f6188d0"

SRC_URI = "${REPO};branch=${BRANCH}"
SRC_URI += "file://0001-Support-both-pre-6.4.0-and-current-i2c-probing.patch"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"
