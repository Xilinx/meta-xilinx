SUMMARY = "AMD Zynq-7000 CAN controller driver (baremetal)."
DESCRIPTION = "Baremetal driver for the legacy AMD Zynq-7000 CAN \
controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "can"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/can/src/"
ESW_COMPONENT_NAME = "libcan.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
