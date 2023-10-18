inherit features_check

REQUIRED_MACHINE_FEATURES = "clockps"

inherit esw

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/clockps/src/"
ESW_COMPONENT_NAME = "libclockps.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
