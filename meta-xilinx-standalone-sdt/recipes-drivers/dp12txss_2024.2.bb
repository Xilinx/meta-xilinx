
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp12txss"

inherit esw python3native

DEPENDS += "xilstandalone dual-splitter vtc dp12 tmrctr"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "hdcp1x", "hdcp1x", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp12txss/src/"
ESW_COMPONENT_NAME = "libdp12txss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
