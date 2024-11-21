FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# TODO: Two patches are disabled as they do not apply to the current version of glmark2
#       This will need review by someone familiar with that code
SRC_URI:append = " file://0001-Make-RGB565-as-default-EGLconfig.patch \
                   file://0001-src-options.cpp-Add-options-to-configure-bpp-and-dep.patch;apply=0 \
                   file://0001-src-gl-state-egl-Use-native_display-to-load-EGL-func.patch;apply=0 \
		"

PACKAGECONFIG = " \
  ${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', 'x11-gl x11-gles2', '', d)} \
  ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'drm-gles2 wayland-gles2', '', d)} \
  ${@bb.utils.contains('DISTRO_FEATURES', 'fbdev', 'fbdev-glesv2', '', d)} \
"
