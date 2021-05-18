PACKAGECONFIG_mali400 = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11 xcb', '', d)} \
                         egl glesv2"

# OpenGL comes from libmali
DEPENDS_append_mali400 = " libmali-xlnx"

PACKAGE_ARCH_mali400 = "${SOC_VARIANT_ARCH}"
