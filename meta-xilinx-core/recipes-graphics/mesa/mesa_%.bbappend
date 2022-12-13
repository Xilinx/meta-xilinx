FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-DRI_Add_xlnx_dri.patch"

# This is not compatible with the mali400 driver, use mesa-gl instead
CONFLICT_DISTRO_FEATURES:class-target = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', bb.utils.contains('DISTRO_FEATURES', 'libmali', 'libmali', '', d), '', d)}"

# Enable lima if not using libmali
PACKAGECONFIG_MALI = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', '', 'lima', d)}"
PACKAGECONFIG:append = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${PACKAGECONFIG_MALI}', '', d)}"

PACKAGE_ARCH_DEFAULT := "${PACKAGE_ARCH}"
PACKAGE_ARCH = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${MACHINE_ARCH}', '${PACKAGE_ARCH_DEFAULT}', d)}"
