
inherit features_check

REQUIRED_MACHINE_FEATURES = "dp14"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dp14/src/"
ESW_COMPONENT_NAME = "libdp14.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
