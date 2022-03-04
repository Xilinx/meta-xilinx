# OpenGL comes from libmali on ev/eg, when egl is enabled
# Adjust the configuration if we're using libmali for this distro
DEFAULT_PACKAGECONFIG := "${PACKAGECONFIG}"

PACKAGECONFIG_LIBMALI = " \
  ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11 xcb', '', d)} \
  egl glesv2 \
  trace \
"

PACKAGECONFIG = "${@'${PACKAGECONFIG_LIBMALI}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGECONFIG}'}"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"

