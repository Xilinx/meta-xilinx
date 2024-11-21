SUMMARY = "Firmware for VDU"
DESCRIPTION = "Firmware binaries provider for VDU"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=722a9d20bf58ac06585a6d91ee36e60e"

PV .= "+git"

S = "${WORKDIR}/git"
B = "${S}"

BRANCH ?= "xlnx_rel_v2024.1"
REPO ?= "git://github.com/Xilinx/vdu-firmware.git;protocol=https"
SRCREV ?= "724de80630edcb87d865d69f1a6c0dc61c3f9f12"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI   = "${REPO};${BRANCHARG}"

inherit autotools features_check

REQUIRED_MACHINE_FEATURES = "vdu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE +="INSTALL_PATH=${D}/${nonarch_base_libdir}/firmware"

do_compile[noexec] = "1"
do_install[dirs] = "${S}"

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
FILES:${PN} = "${nonarch_base_libdir}/firmware/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"

INSANE_SKIP:${PN} = "ldflags"
