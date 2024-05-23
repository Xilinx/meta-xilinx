SUMMARY = "VCU2 decoder/encoder API Includes"
DESCRIPTION = "Include directory for VCU2 encoder/decoder software API"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=003bf8ee942bb6256905b58e9b1b19c2"

PV .= "+git"

BRANCH ?= "xlnx_rel_v2024.1"
REPO   ?= "git://github.com/Xilinx/vcu2-ctrl-sw.git;protocol=https"
SRCREV = "c696fa059781a7a21e8bbf7d9073ce5080155c8a"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S  = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#RDEPENDS:${PN} = "kernel-module-vcu2"

EXTRA_OEMAKE = "CC='${CC}' CXX='${CXX} ${CXXFLAGS}'"

# skip compile (compiled as part of vcu2-app recipe)
do_compile[noexec] = '1'

do_install() {
    install -d ${D}${includedir}/vcu2-ctrl-sw/
    oe_runmake install_headers INSTALL_HDR_PATH=${D}${includedir}/vcu2-ctrl-sw/
}

FILES:${PN} = "usr/include/vcu2-ctrl-sw/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
