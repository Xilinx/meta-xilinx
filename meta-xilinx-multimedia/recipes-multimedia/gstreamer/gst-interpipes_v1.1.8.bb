SUMMARY = "Gstreamer Interpipe 1.1.8"
DESCRIPTION = "GStreamer plug-in that allows communication between two independent pipelines"
HOMEPAGE = "https://developer.ridgerun.com/wiki/index.php?title=GstInterpipe"
SECTION = "multimedia"
LICENSE = "LGPL-2.1-only"

LIC_FILES_CHKSUM = "file://COPYING;md5=3191ae9476980e87e3494d2d8ebe4584"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gtk-doc-native"

SRCBRANCH ?= "master"
# v1.1.8 Tag
SRCREV = "814982ecd735e42ff2d14ce7c43039c259ec928b"
SRC_URI = "gitsm://github.com/RidgeRun/gst-interpipe.git;protocol=https;branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

FILES:${PN} += "${libdir}/gstreamer-1.0/libgstinterpipe.so"

EXTRA_OEMESON = "-Denable-gtk-doc=false"

inherit meson pkgconfig
