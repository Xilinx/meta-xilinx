SRC_URI:append:zynqmp = " file://0001-libweston-Remove-substitute-format-for-ARGB8888.patch"

# Due to the SRC_URI zynqmp specific change, this needs to be SOC_FAMILY_ARCH specific
SOC_FAMILY_ARCH ??= "${TUNE_PKGARCH}"
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
DEFAULT_PACKAGE_ARCH:zynqmp = "${SOC_FAMILY_ARCH}"
PACKAGE_ARCH = "${DEFAULT_PACKAGE_ARCH}"


# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
