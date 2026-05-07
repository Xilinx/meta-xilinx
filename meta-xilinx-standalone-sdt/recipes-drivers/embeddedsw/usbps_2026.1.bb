SUMMARY = "AMD Zynq-7000 PS USB controller driver (baremetal)."

DESCRIPTION = "Baremetal driver for the AMD Zynq-7000 PS-side USB 2.0 \
controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "usbps"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/usbps/src/"
ESW_COMPONENT_NAME = "libusbps.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
