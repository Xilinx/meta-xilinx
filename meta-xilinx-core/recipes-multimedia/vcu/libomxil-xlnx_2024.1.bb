SUMMARY = "OpenMAX Integration layer for VCU"
DESCRIPTION = "OMX IL Libraries,test applications and headers for VCU"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ef69c2bb405668101824f0b644631e2e"

XILINX_VCU_VERSION = "1.0.0"
PV = "${XILINX_VCU_VERSION}-xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}+git${SRCPV}"

BRANCH ?= "master"
REPO   ?= "git://github.com/Xilinx/vcu-omx-il.git;protocol=https"
SRCREV = "e906a887deca1286e9744dd3c128adf11e281538"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S  = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libvcu-xlnx"
RDEPENDS:${PN} = "kernel-module-vcu libvcu-xlnx"

EXTERNAL_INCLUDE="${STAGING_INCDIR}/vcu-ctrl-sw/include"

EXTRA_OEMAKE = " \
    CC='${CC}' CXX='${CXX} ${CXXFLAGS}' \
    EXTERNAL_INCLUDE='${EXTERNAL_INCLUDE}' \
    "

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/vcu-omx-il

    install -m 0644 ${S}/omx_header/*.h ${D}${includedir}/vcu-omx-il

    oe_runmake install INSTALL_PATH=${D}${bindir}

    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.core ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.video_decoder ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.video_encoder ${D}/${libdir}/
}

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"
