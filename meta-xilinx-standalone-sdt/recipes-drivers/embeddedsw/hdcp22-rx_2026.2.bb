
inherit features_check

REQUIRED_MACHINE_FEATURES = "hdcp22-rx"

inherit esw python3native

DEPENDS += "xilstandalone tmrctr hdcp22-common hdcp22-cipher hdcp22-mmult hdcp22-rng"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/hdcp22_rx/src/"
ESW_COMPONENT_NAME = "libhdcp22_rx.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
