
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp14txss"

inherit esw python3native

DEPENDS += "xilstandalone dual-splitter vtc dp14 tmrctr hdcp1x hdcp22-tx-dp"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp14txss/src/"
ESW_COMPONENT_NAME = "libdp14txss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
