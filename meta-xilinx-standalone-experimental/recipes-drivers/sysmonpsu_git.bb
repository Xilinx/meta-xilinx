inherit features_check

REQUIRED_MACHINE_FEATURES = "sysmonpsu"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/sysmonpsu/src/"
ESW_COMPONENT_NAME = "libsysmonpsu.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
