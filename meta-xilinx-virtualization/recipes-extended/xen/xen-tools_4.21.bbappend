require xen-xilinx_4.21.inc
require xen-tools-xilinx.inc

# Workaround to fix do_package QA Issue.
RDEPENDS:${PN} += "\
    ${PN}-libxenmanage \
    "

PACKAGES += " \
    ${PN}-libxenmanage \
    ${PN}-libxenmanage-dev \
    "

FILES:${PN}-staticdev += "\
    ${libdir}/libxenmanage.a \
    "

FILES:${PN}-libxenmanage = "${libdir}/libxenmanage.so.*"
FILES:${PN}-libxenmanage-dev = " \
    ${libdir}/libxenmanage.so \
    ${libdir}/pkgconfig/xenmanage.pc \
    ${datadir}/pkgconfig/xenmanage.pc \
    "

FILES:${PN}-xen-watchdog += "\
    ${systemd_unitdir}/system-sleep/xen-watchdog-sleep.sh \
    "

FILES:${PN}-test += "\
    ${libdir}/xen/tests/test-xenstore \
    ${libdir}/xen/tests/test-rangeset \
    ${libdir}/xen/tests/test-resource \
    ${libdir}/xen/tests/test-domid \
    ${libdir}/xen/tests/test-paging-mempool \
    ${libdir}/xen/tests/test_vpci \
    ${libdir}/xen/tests/test-pdx-offset \
    ${libdir}/xen/tests/test-pdx-mask \
    ${libdir}/xen/tests/test-cpu-policy \
    ${libdir}/xen/tests/test-tsx \
    "
