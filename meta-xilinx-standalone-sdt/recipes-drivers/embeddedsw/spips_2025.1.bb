SUMMARY = "AMD Zynq PS SPI controller driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side SPI controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "spips"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/spips/src/"
ESW_COMPONENT_NAME = "libspips.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
