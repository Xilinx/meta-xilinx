SUMMARY = "Linux kernel module for Video Code Unit"
DESCRIPTION = "Out-of-tree VCU decoder, encoder and common kernel modules provider for MPSoC EV devices"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

XILINX_VCU_VERSION = "1.0.0"
PV = "${XILINX_VCU_VERSION}"

S = "${WORKDIR}/git"

BRANCH ?= "master"
REPO ?= "git://github.com/xilinx/vcu-modules.git;protocol=https"
SRCREV ?= "895ff1d6bd48e96db59d343f1626aae40eca2dd9"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

RDEPENDS_${PN} = "vcu-firmware"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"

PACKAGE_ARCH = "${SOC_FAMILY}"
