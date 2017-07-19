SUMMARY = "rfdc Library"
SECTION = "rfdc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pkgconfig

REPO ?= "git://gitenterprise.xilinx.com/uspea/uspea.git;protocol=https"
BRANCH ?= "master"
SRCREV = "cdfe336d88c31ba882b37e36a69fb4762332540b"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = " \
        ${REPO};${BRANCHARG} \
        file://0001-Makefile-Add-versioning-to-libraries.patch \
        "
S = "${WORKDIR}/git/rfdc/src"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libmetal virtual/fsbl"

PROVIDES = "rfdc"

STAGING_RFDC_DIR = "${TMPDIR}/work-shared/${MACHINE}/rfdc-source"

do_configure() {
    cp ${STAGING_RFDC_DIR}/xrfdc_g.c ${S}
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    cp -P librfdc.so* ${D}${libdir}
    install -m 0644 ${S}/xrfdc_hw.h ${D}${includedir}/xrfdc_hw.h
    install -m 0644 ${S}/xrfdc.h ${D}${includedir}/xrfdc.h
}

FILES_${PN} = "${libdir}/*.so.* ${includedir}/*"
FILES_${PN}-dev = "${libdir}/*.so ${includedir}/*"
