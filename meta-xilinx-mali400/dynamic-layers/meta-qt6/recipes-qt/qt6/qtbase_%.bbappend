FILESEXTRAPATHS:prepend := "${THISDIR}/qtbase:"

# wayland EGL bind-context crash fix, moved here from qtwayland (Qt5).
SRC_URI += " file://0004-qwaylandeglwindow-Bind-context-before-eglDestroySurface.patch"

# Links to mesa / libmali-xlnx, so it becomes MACHINE_ARCH specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

require ${@bb.utils.contains('MACHINE_FEATURES', 'mali400', 'qtbase_mali400.inc', '', d)}
