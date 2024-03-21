SUMMARY = "Firmware for VDU"
DESCRIPTION = "Firmware binaries provider for VDU"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=c5784f63397086d836580d8785d1deb9"

XILINX_VDU_VERSION = "1.0.0"
PV =. "${XILINX_VDU_VERSION}-xilinx-v"
PV .= "+git${SRCPV}"

S  = "${WORKDIR}/git"

inherit autotools features_check

REQUIRED_MACHINE_FEATURES = "vdu"

BRANCH ?= "xlnx_rel_v2023.1"
REPO ?= "git://github.com/Xilinx/vdu-firmware.git;protocol=https"
SRCREV ?= "63fe2fce6e46d5bf03e33300a58a37d8568722ee"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI   = "${REPO};${BRANCHARG}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-ai-core = "versal-ai-core"
COMPATIBLE_MACHINE:versal-ai-edge = "versal-ai-edge"

PACKAGE_ARCH = "${SOC_FAMILY_ARCH}"
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
