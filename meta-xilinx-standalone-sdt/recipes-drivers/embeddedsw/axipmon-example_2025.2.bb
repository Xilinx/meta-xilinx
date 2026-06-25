SUMMARY = "AMD AXI Performance Monitor driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI Performance Monitor \
(APM) IP block in the embeddedsw stack. This recipe builds the driver \
self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "axipmon"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axipmon/examples/"
