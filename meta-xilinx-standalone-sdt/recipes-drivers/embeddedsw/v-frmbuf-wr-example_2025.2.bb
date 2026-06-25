SUMMARY = "AMD Video Frame Buffer Write driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Video Frame Buffer Write \
IP block in the embeddedsw stack. This recipe builds the driver \
self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "v-frmbuf-wr"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_frmbuf_wr/examples/"
