inherit features_check

REQUIRED_MACHINE_FEATURES = "i3cpsx"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/i3cpsx/src/"
ESW_COMPONENT_NAME = "libi3cpsx.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
