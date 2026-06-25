SUMMARY = "AMD AXI DMA controller driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI DMA controller, \
providing scatter/gather and simple DMA transfer primitives in the \
embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "axidma"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axidma/src/"
ESW_COMPONENT_NAME = "libaxidma.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
