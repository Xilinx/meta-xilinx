SUMMARY = "Linux dfeprach library"
DESCRIPTION = "Linux user-space library for the AMD PRACH (DFE-PRACH) \
PL DSP IP block, used in Versal RFSoC / Versal Adaptive Compute DFE \
designs to configure and drive the LTE/NR Physical Random Access \
Channel processor at runtime."
SECTION = "libdfeprach"
LICENSE = "BSD"

inherit pkgconfig xlnx-embeddedsw features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libmetal"

PROVIDES = "libdfeprach"

DFEMIX_SUBDIR = "XilinxProcessorIPLib/drivers/dfeprach/src"

do_compile:prepend() {
    cd ${S}/${DFEMIX_SUBDIR}
    install Makefile.Linux Makefile
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
