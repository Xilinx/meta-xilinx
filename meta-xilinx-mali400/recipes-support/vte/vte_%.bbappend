# vte optionally depends on gtk4, which in turn depends on mesa or mali

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles2') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'gtk4', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
