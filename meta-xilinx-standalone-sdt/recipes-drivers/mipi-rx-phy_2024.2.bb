
inherit features_check

REQUIRED_MACHINE_FEATURES = "mipi-rx-phy"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/mipi_rx_phy/src/"
ESW_COMPONENT_NAME = "libmipi_rx_phy.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
