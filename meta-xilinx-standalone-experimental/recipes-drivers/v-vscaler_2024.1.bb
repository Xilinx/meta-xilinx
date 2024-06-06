inherit features_check

REQUIRED_MACHINE_FEATURES = "v-vscaler"

inherit esw python3native

DEPENDS += "xilstandalone  video-common"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_vscaler/src/"
ESW_COMPONENT_NAME = "libv_vscaler.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
