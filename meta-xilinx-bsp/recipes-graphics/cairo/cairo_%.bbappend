PACKAGECONFIG_zynqmp += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11 xcb', '', d)} \
                         egl glesv2"

# OpenGL comes from libmali on ev/eg
DEPENDS_append_zynqmp-ev = " libmali-xlnx"
DEPENDS_append_zynqmp-eg = " libmali-xlnx"

PACKAGE_ARCH_zynqmp-ev = "${SOC_VARIANT_ARCH}"
PACKAGE_ARCH_zynqmp-eg = "${SOC_VARIANT_ARCH}"
