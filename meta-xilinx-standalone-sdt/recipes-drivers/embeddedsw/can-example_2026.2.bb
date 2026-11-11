SUMMARY = "AMD Zynq-7000 CAN controller driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the legacy AMD Zynq-7000 CAN \
controller in the embeddedsw stack. This recipe builds the driver \
self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "can"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/can/examples/"
