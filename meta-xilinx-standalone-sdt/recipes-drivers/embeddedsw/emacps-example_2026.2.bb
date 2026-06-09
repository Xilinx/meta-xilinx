SUMMARY = "AMD Zynq Cadence GEM Ethernet driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side Cadence GEM Ethernet MAC in the embeddedsw stack. This \
recipe builds the driver self-test/example ELF that ships alongside \
the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "emacps"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/emacps/examples/"
