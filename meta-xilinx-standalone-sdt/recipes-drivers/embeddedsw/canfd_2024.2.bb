SUMMARY = "AMD CAN FD controller driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD CAN FD (Flexible \
Data-rate) controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "canfd"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/canfd/src/"
ESW_COMPONENT_NAME = "libcanfd.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
