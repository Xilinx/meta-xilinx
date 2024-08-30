
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-sditxss"

inherit esw python3native

DEPENDS += "xilstandalone v-sditx vtc"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_sditxss/src/"
ESW_COMPONENT_NAME = "libv_sditxss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
