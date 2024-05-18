SUMMARY = "Firmware for VCU"
DESCRIPTION = "Firmware binaries provider for VCU"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=52eb1e8f27e0e189b175c7d75f028cc6"

PV .= "+git"

S  = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2023.2"
REPO ?= "git://github.com/Xilinx/vcu-firmware.git;protocol=https"
SRCREV = "f4ab98d26aa3e244a487f518f5a76071137c8402"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI   = "${REPO};${BRANCHARG}"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -Dm 0644 ${S}/1.0.0/lib/firmware/al5d_b.fw ${D}${nonarch_base_libdir}/firmware/al5d_b.fw
    install -Dm 0644 ${S}/1.0.0/lib/firmware/al5d.fw ${D}${nonarch_base_libdir}/firmware/al5d.fw
    install -Dm 0644 ${S}/1.0.0/lib/firmware/al5e_b.fw ${D}${nonarch_base_libdir}/firmware/al5e_b.fw
    install -Dm 0644 ${S}/1.0.0/lib/firmware/al5e.fw ${D}${nonarch_base_libdir}/firmware/al5e.fw
}

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
FILES:${PN} = "${nonarch_base_libdir}/firmware/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"

INSANE_SKIP:${PN} = "ldflags"
