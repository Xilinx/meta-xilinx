SUMMARY = "Xilinx DisplayPort Linux Kernel module"
DESCRIPTION = "Out-of-tree DisplayPort(DP) kernel modules provider for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

XLNX_DP_VERSION = "6.1.0"
PV = "${XLNX_DP_VERSION}+xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}+git${SRCPV}"

S = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2023.2"
REPO   ?= "git://github.com/xilinx/dp-modules.git;protocol=https"
SRCREV ?= "5b0969ac09f301c33bccc140c8f60e832f5cf222"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
