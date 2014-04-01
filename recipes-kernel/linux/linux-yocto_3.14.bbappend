
require linux-machine-common.inc

COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

MACHINE_KCONFIG_append_zynq += "common/linux/zynq/defconfig_3.10.cfg"
MACHINE_KCONFIG_append_microblaze += "common/linux/microblaze/defconfig_3.10.cfg"
