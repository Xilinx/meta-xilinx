SUMMARY = "AMD AXI Central DMA controller driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI Central DMA (CDMA) \
controller, providing memory-to-memory DMA transfer primitives in the \
embeddedsw stack. This recipe builds the driver self-test/example ELF \
that ships alongside the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "axicdma"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/axicdma/examples/"
