
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-axi4s-remap"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_axi4s_remap/src/"
ESW_COMPONENT_NAME = "libv_axi4s_remap.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
