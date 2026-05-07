SUMMARY = "AMD MIPI CSI-2 transmitter controller driver (baremetal)."

DESCRIPTION = "Baremetal driver for the AMD MIPI CSI-2 transmitter \
controller IP block in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "csi2tx"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/csi2tx/src/"
ESW_COMPONENT_NAME = "libcsi2tx.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
