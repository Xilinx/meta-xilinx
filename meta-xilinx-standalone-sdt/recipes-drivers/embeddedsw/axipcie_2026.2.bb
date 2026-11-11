SUMMARY = "AMD AXI PCI Express bridge driver (baremetal)."

DESCRIPTION = "Baremetal driver for the AMD AXI PCIe root-complex / \
endpoint bridge IP block in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "axipcie"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axipcie/src/"
ESW_COMPONENT_NAME = "libaxipcie.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
