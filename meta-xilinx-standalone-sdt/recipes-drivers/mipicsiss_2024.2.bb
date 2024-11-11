
inherit features_check

REQUIRED_MACHINE_FEATURES = "mipicsiss"

inherit esw python3native

DEPENDS += "xilstandalone csi iic"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "dphy", "dphy", "",d)}"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "mipi-rx-phy", "mipi-rx-phy", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/mipicsiss/src/"
ESW_COMPONENT_NAME = "libmipicsiss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
