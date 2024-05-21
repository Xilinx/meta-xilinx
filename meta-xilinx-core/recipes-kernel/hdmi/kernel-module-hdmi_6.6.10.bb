SUMMARY = "Xilinx HDMI Linux Kernel module"
DESCRIPTION = "Out-of-tree HDMI kernel modules provider for MPSoC EG/EV devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=b34277fe156508fd5a650609dc36d1fe"

PV .= "+git"

S = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2024.1"
REPO   ?= "git://github.com/Xilinx/hdmi-modules.git;protocol=https"
SRCREV = "edd297762e0bac3f4c5b64ef67244968e22020e2"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI += "file://0001-Support-both-pre-6.4.0-and-current-kernels.patch"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
