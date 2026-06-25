
inherit features_check

REQUIRED_MACHINE_FEATURES = "prd"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/prd/src/"
ESW_COMPONENT_NAME = "libprd.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
