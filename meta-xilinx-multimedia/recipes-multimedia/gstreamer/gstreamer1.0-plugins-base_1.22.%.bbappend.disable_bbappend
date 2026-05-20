require gstreamer-xilinx-1.22.%.inc

SRC_URI:append = " \
           file://0001-ENGR00312515-get-caps-from-src-pad-when-query-caps.patch \
           file://0003-viv-fb-Make-sure-config.h-is-included.patch \
           file://0002-ssaparse-enhance-SSA-text-lines-parsing.patch \
           "

PACKAGECONFIG:append = " opus"

S = "${WORKDIR}/git/subprojects/gst-plugins-base"
