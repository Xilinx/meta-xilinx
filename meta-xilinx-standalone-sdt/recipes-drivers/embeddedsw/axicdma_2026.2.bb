SUMMARY = "AMD AXI Central DMA controller driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI Central DMA (CDMA) \
controller, providing memory-to-memory DMA transfer primitives in the \
embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "axicdma"

inherit esw python3native

DEPENDS += "xilstandalone"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axicdma/src/"
ESW_COMPONENT_NAME = "libaxicdma.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
