SUMMARY = "AMD AXI DMA controller driver self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI DMA controller, \
providing scatter/gather and simple DMA transfer primitives in the \
embeddedsw stack. This recipe builds the driver self-test/example ELF \
that ships alongside the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "axidma"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axidma/examples/"
