SUMMARY = "OpenMAX Integration layer for VCU"
DESCRIPTION = "OMX IL Libraries,test applications and headers for VCU"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ac7c6b649ce8caa4f4c517aaa77c8b17"

# Recipe has been renamed
PROVIDES += "libomxil-xlnx"

PE = "1"

# Version from core/core_version.mk
PV .= "+git"

BRANCH ?= "master"
REPO   ?= "git://github.com/Xilinx/vcu-omx-il.git;protocol=https"
SRCREV = "d801b0ce1e87867bdf4c0009da02f93ffd3341e2"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S  = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "vcu-ctrlsw"
RDEPENDS:${PN} = "libvcu-ctrlsw"

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
