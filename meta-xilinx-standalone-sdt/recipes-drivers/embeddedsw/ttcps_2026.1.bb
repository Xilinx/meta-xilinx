SUMMARY = "AMD Zynq PS triple-timer counter driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side triple-timer counter (TTC) in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "ttcps"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/ttcps/src/"
ESW_COMPONENT_NAME = "libttcps.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
