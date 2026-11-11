SUMMARY = "AMD MIPI D-PHY driver (baremetal)."

DESCRIPTION = "Baremetal driver for the AMD MIPI D-PHY IP block in the \
embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "dphy"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dphy/src/"
ESW_COMPONENT_NAME = "libdphy.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
