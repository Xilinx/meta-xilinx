SUMMARY = "Linux dfeccf library"
SECTION = "libdfeccf"
LICENSE = "BSD"

inherit pkgconfig xlnx-embeddedsw features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libmetal"

PROVIDES = "libdfeccf"

DFECCF_SUBDIR = "XilinxProcessorIPLib/drivers/dfeccf/src"

do_compile:prepend() {
    cd ${S}/${DFECCF_SUBDIR}
    cp Makefile.Linux Makefile
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}

    cd ${S}/${DFECCF_SUBDIR}
    oe_libinstall -so libdfeccf ${D}${libdir}
    install -m 0644 xdfeccf_hw.h ${D}${includedir}/xdfeccf_hw.h
    install -m 0644 xdfeccf.h ${D}${includedir}/xdfeccf.h
}

FILES:${PN} = "${libdir}/*.so.*"
FILES:${PN}-dev = "${libdir}/*.so  ${includedir}/*"
