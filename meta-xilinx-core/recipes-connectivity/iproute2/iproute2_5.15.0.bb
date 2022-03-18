require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://0001-libc-compat.h-add-musl-workaround.patch \
           file://0001-iplink_can-fix-configuration-ranges-in-print_usage-a.patch \
           file://0002-iplink_can-code-refactoring-of-print_ctrlmode.patch \
           file://0003-iplink_can-use-PRINT_ANY-to-factorize-code-and-fix-s.patch \
           file://0004-iplink_can-print-brp-and-dbrp-bittiming-variables.patch \
           "

SRC_URI[sha256sum] = "38e3e4a5f9a7f5575c015027a10df097c149111eeb739993128e5b2b35b291ff"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE:append = " CCOPTS='${CFLAGS}'"
