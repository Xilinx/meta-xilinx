SUMMARY = "Control Software for VDU"
DESCRIPTION = "Control software libraries, test applications and headers provider for VDU deconder software API"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=002a0a92906100955ea6ed02dcd2c2cd"

# Recipe has been renamed
PROVIDES += "libvdu-ctrlsw"

PV .= "+git"

BRANCH ?= "xlnx_rel_v2024.2"
REPO   ?= "git://github.com/Xilinx/vdu-ctrl-sw.git;protocol=https"
SRCREV ?= "361a822a223dc430ca44641be148fe1cbc13dd10"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S = "${WORKDIR}/git"
B = "${S}"

inherit autotools features_check

REQUIRED_MACHINE_FEATURES = "vdu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} = "kernel-module-vdu"
RDEPENDS:libvdu-ctrlsw = "kernel-module-vdu"

do_compile[dirs] = "${S}"
do_install[dirs] = "${S}"

EXTRA_OEMAKE = "CC='${CC}' CXX='${CXX} ${CXXFLAGS}'"
EXTRA_OEMAKE +=" INSTALL_HDR_PATH=${D}${includedir}/vdu-ctrl-sw/include INSTALL_PATH=${D}${bindir}"

do_install:append() {

    oe_libinstall -C ${S}/bin/ -so liballegro_decode ${D}/${libdir}/
}

PACKAGES =+ "libvdu-ctrlsw"
FILES:libvdu-ctrlsw += "${libdir}/liballegro*.so.*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
