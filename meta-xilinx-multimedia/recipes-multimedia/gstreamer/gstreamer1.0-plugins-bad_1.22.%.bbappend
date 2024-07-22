require gstreamer-xilinx-1.22.%.inc

SRC_URI:append = " \
           file://0001-fix-maybe-uninitialized-warnings-when-compiling-with.patch \
           file://0002-avoid-including-sys-poll.h-directly.patch \
           file://0004-opencv-resolve-missing-opencv-data-dir-in-yocto-buil.patch \
           "

PACKAGECONFIG[mediasrcbin] = "-Dmediasrcbin=enabled,-Dmediasrcbin=disabled,media-ctl"
PACKAGECONFIG:append = " faac kms faad opusparse mediasrcbin"

S = "${WORKDIR}/git/subprojects/gst-plugins-bad"
