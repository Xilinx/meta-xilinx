inherit features_check

REQUIRED_MACHINE_FEATURES = "tmr-inject"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/tmr_inject/src/"
ESW_COMPONENT_NAME = "libtmr_inject.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
