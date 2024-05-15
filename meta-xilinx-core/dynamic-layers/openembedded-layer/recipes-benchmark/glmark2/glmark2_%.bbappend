FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0002-native-state-fbdev-Add-support-for-glmark2-es2-fbdev.patch \
    file://0003-EGL-eglplatform.h-Remove-the-eglplatform.h-header.patch  \
    file://0001-Resolve-macro-redefination-and-presion-differ-error.patch \
"

PACKAGECONFIG[fbdev-glesv2] = ",,virtual/libgles2 virtual/egl"

EXTRA_OECONF:append = "${@bb.utils.contains('DISTRO_FEATURES', 'fbdev', ' --with-flavors=fbdev-glesv2', '', d)}"
