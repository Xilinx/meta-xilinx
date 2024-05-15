FILESEXTRAPATHS:prepend := "${THISDIR}/mesa-demos:"

SRC_URI += " \
    file://0001-src-egl-eglinfo-Align-EXT_platform_device-extension-.patch \
    file://0002-src-egl-eglinfo-Use-EGL_PLATFORM_DEVICE_EXT-only-if-.patch \
    file://libmali-egl-workaround.patch \
"

DEPENDS += "wayland-protocols"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
