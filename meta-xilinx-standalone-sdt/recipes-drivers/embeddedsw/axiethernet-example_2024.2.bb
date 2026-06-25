SUMMARY = "AMD AXI Ethernet MAC driver self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI Ethernet (1G/10G) MAC \
IP block in the embeddedsw stack. This recipe builds the driver \
self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "axiethernet"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axiethernet/examples/"
