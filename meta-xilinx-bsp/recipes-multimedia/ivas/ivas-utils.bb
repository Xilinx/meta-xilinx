SUMMARY = "IVAS util"
DESCRIPTION = "IVAS utils for IVAS SDK"
SECTION = "multimedia"
LICENSE = "Apache-2.0"

include ivas.inc

DEPENDS = "glib-2.0 glib-2.0-native xrt libcap libxml2 bison-native flex-native gstreamer1.0-plugins-base jansson"

inherit meson pkgconfig gettext

LIC_FILES_CHKSUM = "file://../LICENSE;md5=e6d9577dd6743c14fb3056b97887d4a4"

S = "${WORKDIR}/ivas/ivas-utils"

GIR_MESON_ENABLE_FLAG = "enabled"
GIR_MESON_DISABLE_FLAG = "disabled"

FILES_${PN} += "${libdir}/libivasutil.so ${libdir}/libxrtutil.so"
FILES_${PN}-dev = "${includedir}"

#CVE_PRODUCT = "gstreamer"
