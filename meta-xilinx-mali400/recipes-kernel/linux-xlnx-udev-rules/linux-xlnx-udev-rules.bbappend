FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', ' file://99-mali-device.rules', '', d)}"
