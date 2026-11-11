SUMMARY = "AMD Zynq UltraScale+ Generic QSPI driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq UltraScale+ MPSoC \
Generic QSPI (QSPIPSU) controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "qspipsu"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/qspipsu/src/"
ESW_COMPONENT_NAME = "libqspipsu.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
