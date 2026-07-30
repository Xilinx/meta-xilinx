# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'qtdeclarative', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
# Note: qtdeclarative depends on qtbase, which has a dependency on opengl, which triggers dependencies on it's users

