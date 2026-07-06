# Add MicroBlaze Patches
FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI += " \
    file://0012-microblaze-Refresh-all-patches-for-newlib-4.6.0.patch \
    "

EXTRA_OECONF:append:xilinx-standalone = " \
    --disable-newlib-reent-check-verify \
"
