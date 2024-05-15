FILESEXTRAPATHS:prepend := "${THISDIR}/qtbase:"

SRC_URI:append = " \
    file://0002-egl_kms-Modify-the-default-color-format-to-RGB565.patch \
    file://0003-qkmsdevice.cpp-Disable-hw-cursor-as-a-default-option.patch \
"

PACKAGECONFIG:append = " \
  examples accessibility tools libinput fontconfig \
  ${@bb.utils.contains('DISTRO_FEATURES', 'fbdev', 'linuxfb gles2 eglfs', '', d)} \
  ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'gles2 eglfs', '', d)} \
  ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'gles2 eglfs kms gbm', '', d)} \
  "

PACKAGECONFIG:remove = "tests"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"

# There is some sort of parallel make install failure
# Makefile:144: recipe for target 'sub-dbus-install_subtargets' failed
# make[1]: *** [sub-dbus-install_subtargets] Error 2
PARALLEL_MAKEINST = "-j 1"

EXTRA_OEMAKE:task-install = " \
    MAKEFLAGS='${PARALLEL_MAKEINST}' \
    OE_QMAKE_CC='${OE_QMAKE_CC}' \
    OE_QMAKE_CXX='${OE_QMAKE_CXX}' \
    OE_QMAKE_CFLAGS='${OE_QMAKE_CFLAGS}' \
    OE_QMAKE_CXXFLAGS='${OE_QMAKE_CXXFLAGS}' \
    OE_QMAKE_LINK='${OE_QMAKE_LINK}' \
    OE_QMAKE_LDFLAGS='${OE_QMAKE_LDFLAGS}' \
    OE_QMAKE_AR='${OE_QMAKE_AR}' \
    OE_QMAKE_OBJCOPY='${OE_QMAKE_OBJCOPY}' \
    OE_QMAKE_STRIP='${OE_QMAKE_STRIP}' \
    OE_QMAKE_INCDIR_QT='${STAGING_DIR_TARGET}/${OE_QMAKE_PATH_HEADERS}' \
"
