SUMMARY = "AMD Zynq UltraScale+ CAN-PS controller driver \
self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq UltraScale+ MPSoC \
PS-side CAN controller in the embeddedsw stack. This recipe builds the \
driver self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "canps"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/canps/examples/"
