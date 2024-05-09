SUMMARY = "Linux kernel module for Video Decode Unit"
DESCRIPTION = "Out-of-tree VDU decoder common kernel modules"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PV .= "+git"

S = "${WORKDIR}/git"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BRANCH ?= "xlnx_rel_v2024.1"
REPO ?= "git://github.com/Xilinx/vdu-modules.git;protocol=https"
SRCREV ?= "25773344ce1e539e7136c5a30cdee98a6cf490a8"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG} \
    file://99-vdu-enc-dec.rules \
"

inherit module features_check

REQUIRED_MACHINE_FEATURES = "vdu"

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

RDEPENDS:${PN} = "vdu-firmware"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-vdu-enc-dec.rules ${D}${sysconfdir}/udev/rules.d/
}

FILES:${PN} = "${sysconfdir}/udev/rules.d/*"
