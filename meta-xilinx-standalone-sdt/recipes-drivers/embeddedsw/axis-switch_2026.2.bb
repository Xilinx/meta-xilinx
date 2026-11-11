inherit features_check

REQUIRED_MACHINE_FEATURES = "axis-switch"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axis_switch/src/"
ESW_COMPONENT_NAME = "libaxis_switch.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
