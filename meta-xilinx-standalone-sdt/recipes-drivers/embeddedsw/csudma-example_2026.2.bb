SUMMARY = "AMD CSU DMA controller driver self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Configuration Security \
Unit (CSU) DMA controller in the embeddedsw stack. This recipe builds \
the driver self-test/example ELF that ships alongside the driver in \
the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "csudma"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/csudma/examples/"
