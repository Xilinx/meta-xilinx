FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0002-native-state-fbdev-Add-support-for-glmark2-es2-fbdev.patch \
    file://0003-EGL-eglplatform.h-Remove-the-eglplatform.h-header.patch  \
"

PACKAGECONFIG[fbdev-glesv2] = ",,virtual/libgles2 virtual/egl"

EXTRA_OECONF:append = "${@bb.utils.contains('DISTRO_FEATURES', 'fbdev', ' --with-flavors=fbdev-glesv2', '', d)}"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"

