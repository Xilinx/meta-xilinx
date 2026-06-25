SUMMARY = "AMD Zynq PS triple-timer counter driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side triple-timer counter (TTC) in the embeddedsw stack. This \
recipe builds the driver self-test/example ELF that ships alongside \
the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "ttcps"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/ttcps/examples/"
