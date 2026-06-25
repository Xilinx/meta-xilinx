SUMMARY = "AMD Zynq SCUGIC interrupt controller driver \
self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the Arm Generic Interrupt \
Controller (GIC) instance in AMD Zynq / Zynq UltraScale+ MPSoC \
PS-sides, exposed via the embeddedsw SCUGIC API. This recipe builds \
the driver self-test/example ELF that ships alongside the driver in \
the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "scugic"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/scugic/examples/"
