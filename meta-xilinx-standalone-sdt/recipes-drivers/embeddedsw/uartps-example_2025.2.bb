SUMMARY = "AMD Zynq PS UART driver self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side Cadence UART controller in the embeddedsw stack. This \
recipe builds the driver self-test/example ELF that ships alongside \
the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "uartps"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartps/examples/"
