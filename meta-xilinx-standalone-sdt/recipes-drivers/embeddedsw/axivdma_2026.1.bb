SUMMARY = "AMD AXI Video DMA driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI Video DMA (VDMA) \
controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "axivdma"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axivdma/src/"
ESW_COMPONENT_NAME = "libaxivdma.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
