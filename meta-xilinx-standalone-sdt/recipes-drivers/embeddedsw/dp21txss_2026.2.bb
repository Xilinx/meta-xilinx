
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp21txss"

inherit esw python3native

DEPENDS += "xilstandalone video-common dp21 vtc"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "clk-wiz", "clk-wiz", "",d)}"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "dual-splitter", "dual-splitter", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp21txss/src/"
ESW_COMPONENT_NAME = "libdp21txss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
