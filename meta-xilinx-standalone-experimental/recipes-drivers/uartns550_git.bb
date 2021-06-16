inherit features_check
  
REQUIRED_DISTRO_FEATURES = "uartns550"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartns550/src/"
ESW_COMPONENT_NAME = "libuartns550.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

