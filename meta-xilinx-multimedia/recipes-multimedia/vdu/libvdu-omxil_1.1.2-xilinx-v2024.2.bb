SUMMARY = "OpenMAX Integration layer for VDU"
DESCRIPTION = "OMX IL Libraries,test application and headers for VDU"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=002a0a92906100955ea6ed02dcd2c2cd"

PV .= "+git"

BRANCH ?= "xlnx_rel_v2024.2"
REPO   ?= "git://github.com/Xilinx/vdu-omx-il.git;protocol=https"
SRCREV ?= "af9c6e8935799f4dcd579b0164dd05eb039b569d"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git"

inherit autotools features_check

REQUIRED_MACHINE_FEATURES = "vdu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "vdu-ctrlsw"
RDEPENDS:${PN} = "libvdu-ctrlsw"

EXTERNAL_INCLUDE="${STAGING_INCDIR}/vdu-ctrl-sw/include"

do_compile[dirs] = "${S}"
do_install[dirs] = "${S}"

EXTRA_OEMAKE = " \
    CC='${CC}' CXX='${CXX} ${CXXFLAGS}' \
    EXTERNAL_INCLUDE='${EXTERNAL_INCLUDE}' \
    INSTALL_PATH=${D}${bindir} \
    INCLUDE_INST_PATH=${D}${includedir} \
    "

do_install:append() {
    install -d ${D}${libdir}

    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.core ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.video_decoder ${D}/${libdir}/
}

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"
