
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-warp-init"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_warp_init/src/"
ESW_COMPONENT_NAME = "libv_warp_init.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
