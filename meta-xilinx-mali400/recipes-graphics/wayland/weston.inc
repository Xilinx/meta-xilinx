SRC_URI:append:zynqmp = " file://0001-Force-weston-to-select-ARGB-if-XRGB-is-not-available.patch"

# Due to the SRC_URI zynqmp specific change, this needs to be MACHINE_ARCH specific
PACKAGE_ARCH:zynqmp = "${MACHINE_ARCH}"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
