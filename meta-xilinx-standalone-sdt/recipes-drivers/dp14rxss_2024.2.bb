
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp14rxss"

inherit esw python3native

DEPENDS += "xilstandalone video-common dp14 iic iicps hdcp1x hdcp22-rx-dp"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp14rxss/src/"
ESW_COMPONENT_NAME = "libdp14rxss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
