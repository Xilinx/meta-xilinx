SUMMARY = "Xilinx DisplayPort Linux Kernel module"
DESCRIPTION = "Out-of-tree DisplayPort(DP) kernel modules provider for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PV .= "+git"

S = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2025.1"
REPO   ?= "git://github.com/Xilinx/dp-modules.git;protocol=https"
SRCREV ?= "709d08e99f6f7fbd558312ae6bcb3762352be877"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI += "file://0001-Support-both-pre-6.4.0-and-current-i2c-probing.patch"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
