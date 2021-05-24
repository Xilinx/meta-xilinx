# OpenGL comes from libmali on ev/eg, when egl is enabled
DEPENDS_append_mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', ' libmali-xlnx', '', d)}"

PACKAGE_ARCH_mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', '${SOC_VARIANT_ARCH}', '${TUNE_PKGARCH}', d)}"
