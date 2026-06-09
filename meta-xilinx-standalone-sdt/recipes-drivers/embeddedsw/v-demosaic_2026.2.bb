inherit features_check

REQUIRED_MACHINE_FEATURES = "v-demosaic"

inherit esw python3native

DEPENDS += "xilstandalone video-common"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_demosaic/src/"
ESW_COMPONENT_NAME = "libv_demosaic.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
