# ZynqMP DisplayPort workarounds, wrong on other platforms.
#
# The class test is here rather than on the require, because a require is
# processed before the native class override is in effect.
QTBASE_MALI400_PATCHES = ""
QTBASE_MALI400_PATCHES:class-target = " \
    file://0002-egl_kms-Modify-the-default-color-format-to-RGB565.patch \
    file://0003-qkmsdevice.cpp-Disable-hw-cursor-as-a-default-option.patch \
"
SRC_URI += "${QTBASE_MALI400_PATCHES}"

# mali400 comes only from a machine conf, so this is machine specific on
# either GPU driver.
PACKAGE_ARCH = "${MACHINE_ARCH}"
