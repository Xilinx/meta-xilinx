SUMMARY = "Control Software for VCU2"
DESCRIPTION = "Control software libraries, test applications and headers provider for VCU2 encoder/decoder software API"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=624e7970a65c3e70b309116d8fb6c3d7"

PV .= "+git"

BRANCH ?= "master"
REPO   ?= "git://github.com/Xilinx/vcu2-ctrl-sw.git;protocol=https"
SRCREV = "3949f98b2ab8244a6435551737195e5efc588a4b"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"


inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "kernel-module-vcu2"
RDEPENDS:${PN} = "kernel-module-vcu2"
RDEPENDS:libvcu2-ctrlsw = "kernel-module-vcu2"

EXTRA_OEMAKE = "CC='${CC}' CXX='${CXX} ${CXXFLAGS}'"

do_install() {
    install -Dm 0755 ${S}/bin/AL_Encoder.exe ${D}/${bindir}/ctrlsw_encoder
    install -Dm 0755 ${S}/bin/AL_Decoder.exe ${D}/${bindir}/ctrlsw_decoder
    oe_libinstall -C ${S}/bin/ -so liballegro_decode ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_encode ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_app ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_conv_yuv ${D}/${libdir}/

    install -d ${D}${includedir}/vcu2-ctrl-sw/include
    oe_runmake install_headers INSTALL_HDR_PATH=${D}${includedir}/vcu2-ctrl-sw/include
}

PACKAGES =+ "libvcu2-ctrlsw"
FILES:libvcu2-ctrlsw += "${libdir}/liballegro*.so.*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
