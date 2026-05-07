SUMMARY = "AMD Zynq Cortex-A9 private watchdog driver (baremetal)."

DESCRIPTION = "Baremetal driver for the Cortex-A9 private watchdog in \
AMD Zynq-7000 PS-sides, exposed via the embeddedsw SCU watchdog API."
inherit features_check

REQUIRED_MACHINE_FEATURES = "scuwdt"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/scuwdt/src/"
ESW_COMPONENT_NAME = "libscuwdt.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
