
require linux-machine-common.inc

COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

MACHINE_KCONFIG_append_zynq += "common/linux/zynq/defconfig_3.10.cfg"
MACHINE_KCONFIG_append_microblaze += "common/linux/microblaze/defconfig_3.10.cfg"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
SRC_URI_append_microblaze += "file://ec2eba55f0c0e74dd39aca14dcc597583cf1eb67.patch"
