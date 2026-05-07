SUMMARY = "AMD EmacLite (XPS Ethernet Lite) driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD EmacLite (XPS Ethernet \
Lite) MAC IP block in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "emaclite"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/emaclite/src/"
ESW_COMPONENT_NAME = "libemaclite.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
