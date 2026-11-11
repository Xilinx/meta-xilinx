
inherit features_check

REQUIRED_MACHINE_FEATURES = "hdcp22-tx"

inherit esw python3native

DEPENDS += "xilstandalone tmrctr hdcp22-common hdcp22-cipher hdcp22-rng"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/hdcp22_tx/src/"
ESW_COMPONENT_NAME = "libhdcp22_tx.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
