SUMMARY = "Linux kernel module for Video Code Unit"
DESCRIPTION = "Out-of-tree VCU decoder, encoder and common kernel modules provider for MPSoC EV devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PE = "1"

PV .= "+git"


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BRANCH = "master"
REPO = "git://github.com/Xilinx/vcu-modules.git;protocol=https"
SRCREV = "fbe150d6b05679f0b9199671623d2687bc6a8f2a"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = " \
    ${REPO};${BRANCHARG} \
    file://99-vcu-enc-dec.rules \
    "

inherit module features_check

REQUIRED_MACHINE_FEATURES = "vcu"

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

RDEPENDS:${PN} = "vcu-firmware"

KERNEL_MODULE_AUTOLOAD += "dmaproxy"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/99-vcu-enc-dec.rules ${D}${sysconfdir}/udev/rules.d/
}

FILES:${PN} = "${sysconfdir}/udev/rules.d/*"
