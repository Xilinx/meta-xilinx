SUMMARY = "Xilinx HDMI Linux Kernel module"
DESCRIPTION = "Out-of-tree HDMI kernel modules provider for MPSoC EG/EV devices"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=b34277fe156508fd5a650609dc36d1fe"

XLNX_HDMI_VERSION = "5.10.0"
PV = "${XLNX_HDMI_VERSION}"

S = "${WORKDIR}/git"

BRANCH ?= "master"
REPO   ?= "git://github.com/Xilinx/hdmi-modules.git;protocol=https"
SRCREV = "7199973cb7fa91776335be58e38521b77231e9fe"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"
COMPATIBLE_MACHINE_versal = "versal"
