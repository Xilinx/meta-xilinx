# OpenGL comes from libmali on ev/eg, when egl is enabled
DEPENDS:append:mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', ' libmali-xlnx', '', d)}"

PACKAGE_ARCH:mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', '${MACHINE_ARCH}', '${TUNE_PKGARCH}', d)}"
