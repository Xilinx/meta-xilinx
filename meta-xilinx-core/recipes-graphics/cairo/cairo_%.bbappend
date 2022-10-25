PACKAGECONFIG:mali400 = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11 xcb', '', d)} \
                         egl glesv2"

# OpenGL comes from libmali
DEPENDS:append:mali400 = " libmali-xlnx"

PACKAGE_ARCH:mali400 = "${MACHINE_ARCH}"
