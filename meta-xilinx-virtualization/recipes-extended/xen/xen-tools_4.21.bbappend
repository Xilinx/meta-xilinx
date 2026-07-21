require xen-xilinx_4.21.inc
require xen-tools-xilinx.inc

# libxl_internal.c's libxl__dirname() assigns strrchr()'s result to a
# non-const char *; with _FORTIFY_SOURCE=2 newer GCC can propagate the
# constness of the input through its __builtin_strrchr, tripping
# -Werror=discarded-qualifiers.
CFLAGS += "-Wno-error=discarded-qualifiers"

# NOTE: libxenmanage/libxenmanage-dev PACKAGES/FILES/staticdev are already
# declared by xen-tools.inc (libxenmanage is in xen-4.21+). A local
# workaround duplicating them here previously caused a do_package QA error:
#   "xen-tools-libxenmanage is listed in PACKAGES multiple times"

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
