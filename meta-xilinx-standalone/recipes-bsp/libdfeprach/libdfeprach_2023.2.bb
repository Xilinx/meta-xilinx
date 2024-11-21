SUMMARY = "Linux dfeprach library"
SECTION = "libdfeprach"
LICENSE = "BSD"

inherit pkgconfig xlnx-embeddedsw features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libmetal"

PROVIDES = "libdfeprach"

DFEMIX_SUBDIR = "XilinxProcessorIPLib/drivers/dfeprach/src"

do_compile:prepend() {
    cd ${S}/${DFEMIX_SUBDIR}
    cp Makefile.Linux Makefile
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}

    cd ${S}/${DFEMIX_SUBDIR}
    oe_libinstall -so libdfeprach ${D}${libdir}
    install -m 0644 xdfeprach_hw.h ${D}${includedir}/xdfeprach_hw.h
    install -m 0644 xdfeprach.h ${D}${includedir}/xdfeprach.h
}

FILES:${PN} = "${libdir}/*.so.*"
FILES:${PN}-dev = "${libdir}/*.so  ${includedir}/*"
