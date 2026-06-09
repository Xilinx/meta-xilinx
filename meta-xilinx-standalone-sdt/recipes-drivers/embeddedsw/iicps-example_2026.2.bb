SUMMARY = "AMD Zynq PS I2C controller driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side I2C controller in the embeddedsw stack. This recipe \
builds the driver self-test/example ELF that ships alongside the \
driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "iicps"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/iicps/examples/"
