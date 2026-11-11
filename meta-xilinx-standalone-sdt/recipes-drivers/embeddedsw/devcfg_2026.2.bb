SUMMARY = "AMD Zynq-7000 device configuration (DevCfg) driver \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq-7000 Device \
Configuration (DevCfg) controller used for PL programming in the \
embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "devcfg"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/devcfg/src/"
ESW_COMPONENT_NAME = "libdevcfg.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
