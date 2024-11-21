SUMMARY = "Linux dfemix library"
SECTION = "libdfemix"
LICENSE = "BSD"

inherit pkgconfig xlnx-embeddedsw features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libmetal"

PROVIDES = "libdfemix"

DFEMIX_SUBDIR = "XilinxProcessorIPLib/drivers/dfemix/src"

do_compile:prepend() {
    cd ${S}/${DFEMIX_SUBDIR}
    cp Makefile.Linux Makefile
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}

    cd ${S}/${DFEMIX_SUBDIR}
    oe_libinstall -so libdfemix ${D}${libdir}
    install -m 0644 xdfemix_hw.h ${D}${includedir}/xdfemix_hw.h
    install -m 0644 xdfemix.h ${D}${includedir}/xdfemix.h
}

FILES:${PN} = "${libdir}/*.so.*"
FILES:${PN}-dev = "${libdir}/*.so  ${includedir}/*"
