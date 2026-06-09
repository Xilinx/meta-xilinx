
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-sdirxss"

inherit esw python3native

DEPENDS += "xilstandalone v-sdirx"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_sdirxss/src/"
ESW_COMPONENT_NAME = "libv_sdirxss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
