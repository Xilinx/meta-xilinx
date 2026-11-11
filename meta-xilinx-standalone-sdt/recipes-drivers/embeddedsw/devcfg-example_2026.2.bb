SUMMARY = "AMD Zynq-7000 device configuration (DevCfg) driver \
self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq-7000 Device \
Configuration (DevCfg) controller used for PL programming in the \
embeddedsw stack. This recipe builds the driver self-test/example ELF \
that ships alongside the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "devcfg"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/devcfg/examples/"
