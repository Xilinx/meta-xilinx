FILESEXTRAPATHS:prepend:zynqmp := "${THISDIR}/files:"

SRC_URI:append:zynqmp = " file://0001-libweston-Remove-substitute-format-for-ARGB8888.patch"

# Due to the SRC_URI zynqmp specific change, this needs to be SOC_FAMILY_ARCH specific
SOC_FAMILY_ARCH ??= "${TUNE_PKGARCH}"
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
DEFAULT_PACKAGE_ARCH:zynqmp = "${SOC_FAMILY_ARCH}"
PACKAGE_ARCH = "${DEFAULT_PACKAGE_ARCH}"


# mali400 specific items
MALI_SRC_URI = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', 'file://0002-libmali-does-not-support-gles3.patch', '', d)}"
SRC_URI:append = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', ' ${MALI_SRC_URI}', '', d)}"

# Skip dmabuf-feedback, as it requires gbm >= 21.1.1, mali-xlnx only provides 17.3
DEFAULT_SIMPLECLIENTS := "${SIMPLECLIENTS}"
MALI_SIMPLECLIENTS = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', 'damage,im,egl,shm,touch,dmabuf-v4l,dmabuf-egl', '${DEFAULT_SIMPLECLIENTS}', d)}"
SIMPLECLIENTS = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${MALI_SIMPLECLIENTS}', '${DEFAULT_SIMPLECLIENTS}', d)}"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
