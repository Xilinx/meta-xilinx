SUMMARY = "Xilinx DisplayPort Linux Kernel module"
DESCRIPTION = "Out-of-tree DisplayPort (DP) kernel modules for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PV = "6.18.0+git"

S = "${WORKDIR}/git"

REPO   = "git://github.com/Xilinx/dp-modules.git;protocol=https"
BRANCH = "master"
SRCREV = "fb2fe5b1236afd17b51ddc0cb4cd69aa7f80c79f"

SRC_URI = "${REPO};branch=${BRANCH}"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"
