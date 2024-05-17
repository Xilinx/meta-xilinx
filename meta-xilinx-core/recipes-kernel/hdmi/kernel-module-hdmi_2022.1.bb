SUMMARY = "Xilinx HDMI Linux Kernel module"
DESCRIPTION = "Out-of-tree HDMI kernel modules provider for MPSoC EG/EV devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=b34277fe156508fd5a650609dc36d1fe"

XLNX_HDMI_VERSION = "5.15.0"
PV = "${XLNX_HDMI_VERSION}+xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}+git"

S = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2022.1"
REPO   ?= "git://github.com/Xilinx/hdmi-modules.git;protocol=https"
SRCREV = "25b6fe7a26a975be15c002b48cfd4c291486491e"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
