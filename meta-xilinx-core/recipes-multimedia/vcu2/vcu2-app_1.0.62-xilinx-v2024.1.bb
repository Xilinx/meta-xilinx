SUMMARY = "VCU2 decoder/encoder API Includes"
DESCRIPTION = "Include directory for VCU2 encoder/decoder software API"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=003bf8ee942bb6256905b58e9b1b19c2"

PV .= "+git"

BRANCH ?= "xlnx_rel_v2024.1"
# vcu2-app has not been populated yet: get sources from vcu2-ctrlsw instead
#REPO ?= "git://github.com/Xilinx/vcu2-app.git;protocol=https"
#SRCREV = "59c6beb262f09f8e368826a39612844642777924"
REPO   ?= "git://github.com/Xilinx/vcu2-ctrl-sw.git;protocol=https"
SRCREV = "c696fa059781a7a21e8bbf7d9073ce5080155c8a"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S  = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#RDEPENDS:${PN} = "kernel-module-vcu2"

EXTRA_OEMAKE = "V=1 CC='${CC}' CXX='${CXX} ${CXXFLAGS}'"

do_install() {
    install -Dm 0755 ${S}/bin/AL_Encoder.exe ${D}/${bindir}/ctrlsw_encoder
    install -Dm 0755 ${S}/bin/AL_Decoder.exe ${D}/${bindir}/ctrlsw_decoder
    oe_libinstall -C ${S}/bin/ -so liballegro_decode ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_encode ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_app ${D}/${libdir}/
    oe_libinstall -C ${S}/bin/ -so liballegro_conv_yuv ${D}/${libdir}/
}

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
