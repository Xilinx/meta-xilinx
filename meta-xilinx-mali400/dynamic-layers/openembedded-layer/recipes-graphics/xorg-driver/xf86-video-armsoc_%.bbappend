FILESEXTRAPATHS:prepend := "${THISDIR}/xf86-video-armsoc:"

EXTRA_MALI400_SRC = " file://0001-xf86-video-armosc-Accelerate-picture-composition.patch \
                      file://0001-xf86-video-armosc-Option-to-control-acceleration.patch \
                    "
DEPENDS:append = "${@' libmali-xlnx' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else ''}"

SRC_URI:append = "${@bb.utils.contains('DEPENDS', 'libmali-xlnx', '${EXTRA_MALI400_SRC}', '', d)}"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
PACKAGE_ARCH = "${@bb.utils.contains('DEPENDS', 'libmali-xlnx', '${MACHINE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
