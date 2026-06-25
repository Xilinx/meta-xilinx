SUMMARY = "AMD CAN FD controller driver self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD CAN FD (Flexible \
Data-rate) controller in the embeddedsw stack. This recipe builds the \
driver self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "canfd"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/canfd/examples/"
