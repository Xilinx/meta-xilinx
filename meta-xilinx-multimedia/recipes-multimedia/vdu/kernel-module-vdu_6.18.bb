SUMMARY = "Linux kernel module for Video Decode Unit"
DESCRIPTION = "Out-of-tree VDU decoder common kernel modules"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

PE = "1"
PV .= "+git"

S = "${WORKDIR}/git"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BRANCH ?= "master"
REPO ?= "git://github.com/Xilinx/vdu-modules.git;protocol=https"
SRCREV ?= "81b8c58f8d888eda535aeb773f768af134e9347c"

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

SKIP_RECIPE[kernel-module-vdu] = "${@'Only kernel 6.18 and before are supported.' if bb.utils.vercmp_string(d.getVarFlag('XILINX_LINUX_VERSION', d.getVar('XILINX_RELEASE_VERSION')) or 'undefined', "6.19") >= 0 else ''}"
