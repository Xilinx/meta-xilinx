require gstreamer-xilinx-1.22.%.inc

SRC_URI:append = " \
           file://0001-qt-include-ext-qt-gstqtgl.h-instead-of-gst-gl-gstglf.patch \
           file://0001-v4l2-Define-ioctl_req_t-for-posix-linux-case.patch \
           "

PACKAGECONFIG:append = " vpx"

S = "${WORKDIR}/git/subprojects/gst-plugins-good"
