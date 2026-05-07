SUMMARY = "Linux dfeequ library"
DESCRIPTION = "Linux user-space library for the AMD Equaliser \
(DFE-EQU) PL DSP IP block, used in Versal RFSoC / Versal Adaptive \
Compute DFE designs to configure and drive the equaliser at runtime."
SECTION = "libdfeequ"
LICENSE = "BSD"

inherit pkgconfig xlnx-embeddedsw features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libmetal"

PROVIDES = "libdfeequ"

DFEEQU_SUBDIR = "XilinxProcessorIPLib/drivers/dfeequ/src"

do_compile:prepend() {
    cd ${S}/${DFEEQU_SUBDIR}
    install Makefile.Linux Makefile
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}

    cd ${S}/${DFEEQU_SUBDIR}
    oe_libinstall -so libdfeequ ${D}${libdir}
    install -m 0644 xdfeequ_hw.h ${D}${includedir}/xdfeequ_hw.h
    install -m 0644 xdfeequ.h ${D}${includedir}/xdfeequ.h
}

FILES:${PN} = "${libdir}/*.so.*"
FILES:${PN}-dev = "${libdir}/*.so  ${includedir}/*"
