SUMMARY = "Firmware for VDU"
DESCRIPTION = "Firmware binaries provider for VDU"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=c5784f63397086d836580d8785d1deb9"

PV .= "+git"

S  = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2023.2"
REPO ?= "git://github.com/Xilinx/vdu-firmware.git;protocol=https"
SRCREV ?= "731897772730178f6a4e77eedeb4fb53faa1ab4d"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI   = "${REPO};${BRANCHARG}"

inherit autotools features_check

REQUIRED_MACHINE_FEATURES = "vdu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile[noexec] = "1"

EXTRA_OEMAKE +="INSTALL_PATH=${D}/${nonarch_base_libdir}/firmware"

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
FILES:${PN} = "${nonarch_base_libdir}/firmware/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"

INSANE_SKIP:${PN} = "ldflags"
