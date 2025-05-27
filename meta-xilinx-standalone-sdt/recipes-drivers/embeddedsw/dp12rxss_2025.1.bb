
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp12rxss"

inherit esw python3native

DEPENDS += "xilstandalone video-common dp12 iic iicps"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "hdcp1x", "hdcp1x", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp12rxss/src/"
ESW_COMPONENT_NAME = "libdp12rxss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
