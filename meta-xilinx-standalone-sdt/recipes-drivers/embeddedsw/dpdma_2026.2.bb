SUMMARY = "AMD DisplayPort DMA driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD DisplayPort DMA (DPDMA) \
controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "dpdma"

inherit esw python3native

DEPENDS += "xilstandalone  video-common avbuf"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dpdma/src/"
ESW_COMPONENT_NAME = "libdpdma.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
