FILESEXTRAPATHS:prepend := "${THISDIR}/qtbase:"

# wayland EGL bind-context crash fix, moved here from qtwayland (Qt5).
SRC_URI += " file://0004-qwaylandeglwindow-Bind-context-before-eglDestroySurface.patch"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"

require ${@bb.utils.contains('MACHINE_FEATURES', 'mali400', 'qtbase_mali400.inc', '', d)}
