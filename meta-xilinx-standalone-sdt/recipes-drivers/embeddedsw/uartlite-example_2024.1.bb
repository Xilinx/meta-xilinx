SUMMARY = "AMD UART Lite (XPS UART Lite) driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD UART Lite IP block in the \
embeddedsw stack. This recipe builds the driver self-test/example ELF \
that ships alongside the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "uartlite"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartlite/examples/"
