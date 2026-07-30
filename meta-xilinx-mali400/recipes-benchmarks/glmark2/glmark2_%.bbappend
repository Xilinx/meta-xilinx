FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# TODO: Two patches are disabled as they do not apply to the current version of glmark2
#       This will need review by someone familiar with that code
SRC_URI:append = " file://0001-src-options.cpp-Add-options-to-configure-bpp-and-dep.patch;apply=0 \
                   file://0001-src-gl-state-egl-Use-native_display-to-load-EGL-func.patch;apply=0 \
		"

# Mali-400 specific glmark2 changes are only applied on machines that expose the
# mali400 feature (e.g. ZynqMP EG/EV). Loading them through a require keeps
# MACHINE_FEATURES out of this recipe's signature, and avoids leaking the changes
# into machines that pull in this layer for other reasons (e.g. G78AE, which uses
# the malig78ae feature).
require ${@bb.utils.contains('MACHINE_FEATURES', 'mali400', 'glmark2-mali400.inc', '', d)}

PACKAGECONFIG = " \
  ${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', 'x11-gl x11-gles2', '', d)} \
  ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'drm-gles2 wayland-gles2', '', d)} \
  ${@bb.utils.contains('DISTRO_FEATURES', 'fbdev', 'fbdev-glesv2', '', d)} \
"
