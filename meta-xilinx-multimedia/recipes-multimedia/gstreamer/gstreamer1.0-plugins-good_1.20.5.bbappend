PACKAGECONFIG:append = " vpx"

require gstreamer-xilinx-1.20.5.inc

SRC_URI:append = " \
           file://0001-qt-include-ext-qt-gstqtgl.h-instead-of-gst-gl-gstglf.patch \
           "

S = "${WORKDIR}/git/subprojects/gst-plugins-good"
