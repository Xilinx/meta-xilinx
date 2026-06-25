SUMMARY = "AMD Zynq UltraScale+ USB controller driver \
self-test/example (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq UltraScale+ MPSoC \
PS-side USB 3.0 (USBPSU) controller in the embeddedsw stack. This \
recipe builds the driver self-test/example ELF that ships alongside \
the driver in the embeddedsw tree."
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "usbpsu"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/usbpsu/examples/"
