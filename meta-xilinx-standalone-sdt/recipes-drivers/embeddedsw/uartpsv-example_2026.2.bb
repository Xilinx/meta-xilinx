SUMMARY = "AMD Versal PMC/PSM UART (PL011) driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the Arm PL011 UART instance used \
by the AMD Versal PMC/PSM in the embeddedsw stack. This recipe builds \
the driver self-test/example ELF that ships alongside the driver in \
the embeddedsw tree."
inherit esw_examples features_check
  
REQUIRED_MACHINE_FEATURES = "uartpsv"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartpsv/examples/"

