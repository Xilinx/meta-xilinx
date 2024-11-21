FILESEXTRAPATHS:prepend := "${THISDIR}/xf86-video-armsoc:"

SRC_URI:append = " file://0001-src-drmmode_xilinx-Add-the-dumb-gem-support-for-Xili.patch \
                   file://0001-armsoc_driver.c-Bypass-the-exa-layer-to-free-the-roo.patch \
                   file://0001-xf86-video-armsoc-Add-shadow-buffer-hooks.patch \
                 "
EXTRA_MALI400_SRC = " file://0001-xf86-video-armosc-Accelerate-picture-composition.patch \
                      file://0001-xf86-video-armosc-Option-to-control-acceleration.patch \
                    "
SRC_URI:append = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${EXTRA_MALI400_SRC}', '', d)}"

DEPENDS:append = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', ' libmali-xlnx', '', d)}"

