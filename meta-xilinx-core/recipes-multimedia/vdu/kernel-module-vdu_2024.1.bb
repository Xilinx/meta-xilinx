SUMMARY = "Linux kernel module for Video Decode Unit"
DESCRIPTION = "Out-of-tree VDU decoder common kernel modules"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

XILINX_VDU_VERSION = "1.0.0"
PV =. "${XILINX_VDU_VERSION}-xilinx-v"
PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BRANCH ?= "master"
REPO ?= "git://github.com/Xilinx/vdu-modules.git;protocol=https"
SRCREV ?= "1529b6aaf784fb5e1cafb11c949c3cb3c69d1dfd"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG} \
	file://99-vdu-enc-dec.rules \
"

inherit module features_check

REQUIRED_MACHINE_FEATURES = "vdu"

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

RDEPENDS:${PN} = "vdu-firmware"

PACKAGE_ARCH = "${SOC_FAMILY_ARCH}"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-vdu-enc-dec.rules ${D}${sysconfdir}/udev/rules.d/
}

FILES:${PN} = "${sysconfdir}/udev/rules.d/*"
