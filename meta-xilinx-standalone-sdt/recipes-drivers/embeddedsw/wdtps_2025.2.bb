SUMMARY = "AMD Zynq UltraScale+ PS watchdog driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq UltraScale+ MPSoC \
PS-side watchdog controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "wdtps"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/wdtps/src/"
ESW_COMPONENT_NAME = "libwdtps.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
