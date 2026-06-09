SUMMARY = "AMD EmacLite (XPS Ethernet Lite) driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD EmacLite (XPS Ethernet \
Lite) MAC IP block in the embeddedsw stack. This recipe builds the \
driver self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "emaclite"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/emaclite/examples/"
