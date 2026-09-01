# Add MicroBlaze Patches
FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI += " \
    file://mb32/0001-Modified-exceptional-handler-Modified-the-exceptiona.patch \
    file://mb32/0002-Add-missing-declarations-for-xil-printf-to-stdio-h-f.patch \
    file://mb32/0003-deleting-the-xil-printf-c-file-as-now-it-part-of-BSP.patch \
    file://mb32/0004-patch-4-manual-remove-xil-printf-from-Makefile-inc.patch \
    file://mb32/0005-MB-X-intial-commit.patch \
    file://mb64/0006-newlib-port-for-microblaze-m64-flag.patch \
    file://mb64/0007-fixing-the-bug-in-crt-files-added-addlik-instead-of.patch \
    file://mb64/0008-Added-MB-64-support-to-strcmp-strcpy-strlen-files-Si.patch \
    file://mb64/0009-Removing-the-Assembly-implementation-of-64bit-string.patch \
    file://mb64/0010-Fixed-the-bug-in-crtinit-s-for-MB-64.patch \
    file://mb32/0011-libgloss-remove-xil-printf-from-Makefile-in.patch \
    "

EXTRA_OECONF:append:xilinx-standalone = " \
    --disable-newlib-reent-check-verify \
"
