SUMMARY = "OpenMAX Integration layer for VCU2"
DESCRIPTION = "OMX IL Libraries,test applications and headers for VCU2"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=002a0a92906100955ea6ed02dcd2c2cd"

# Version from core/core_version.mk
PV .= "+git"

BRANCH ?= "xlnx_rel_v2024.2"
REPO   ?= "git://github.com/Xilinx/vcu2-omx-il.git;protocol=https"
SRCREV = "130d56172f768e5d2f8c8a3aa75fcbad581ab2d9"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S  = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "vcu2-ctrlsw"
RDEPENDS:${PN} = "libvcu2-ctrlsw"

EXTERNAL_INCLUDE="${STAGING_INCDIR}/vcu2-ctrl-sw/include"

EXTRA_OEMAKE = " \
    CC='${CC}' CXX='${CXX} ${CXXFLAGS}' \
    EXTERNAL_INCLUDE='${EXTERNAL_INCLUDE}' \
    "

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/vcu2-omx-il

    install -m 0644 ${S}/omx_header/*.h ${D}${includedir}/vcu2-omx-il

    install -Dm 0755 ${S}/bin/omx_decoder.exe ${D}/${bindir}/omx_decoder
    install -Dm 0755 ${S}/bin/omx_encoder.exe ${D}/${bindir}/omx_encoder

    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.core ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.video_decoder ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so libOMX.allegro.video_encoder ${D}/${libdir}/
}

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"
