SUMMARY = "AMD Zynq UltraScale+ PS watchdog driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq UltraScale+ MPSoC \
PS-side watchdog controller in the embeddedsw stack. This recipe \
builds the driver self-test/example ELF that ships alongside the \
driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "wdtps"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/wdtps/examples/"
