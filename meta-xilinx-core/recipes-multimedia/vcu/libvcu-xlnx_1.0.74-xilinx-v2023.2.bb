SUMMARY = "Control Software for VCU"
DESCRIPTION = "Control software libraries, test applications and headers provider for VCU"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ef69c2bb405668101824f0b644631e2e"

PV .= "+git"

BRANCH ?= "xlnx_rel_v2023.2"
REPO   ?= "git://github.com/Xilinx/vcu-ctrl-sw.git;protocol=https"
SRCREV = "84b0856cad7844d69f57ac4d9447c20930875475"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI += "file://0001-Support-updated-gcc-add-cstdint-where-necessary.patch"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S  = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"

RDEPENDS:${PN} = "kernel-module-vcu"

EXTRA_OEMAKE = "CC='${CC}' CXX='${CXX} ${CXXFLAGS}'"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/vcu-ctrl-sw/include

    oe_runmake install_headers INSTALL_HDR_PATH=${D}${includedir}/vcu-ctrl-sw/include INSTALL_PATH=${D}/${bindir}
    oe_libinstall -C ${S}/bin/ -so liballegro_decode ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_encode ${D}/${libdir}/
}

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
