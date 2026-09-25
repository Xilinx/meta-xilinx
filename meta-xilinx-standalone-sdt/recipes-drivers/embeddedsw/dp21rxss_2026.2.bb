
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp21rxss"

inherit esw python3native

DEPENDS += "xilstandalone dp21"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "clk-wiz", "clk-wiz", "",d)}"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "iic", "iic", "",d)}"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "iicps", "iicps", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp21rxss/src/"
ESW_COMPONENT_NAME = "libdp21rxss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
