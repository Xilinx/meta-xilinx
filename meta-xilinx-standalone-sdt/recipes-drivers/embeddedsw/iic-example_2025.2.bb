SUMMARY = "AMD AXI IIC (I2C) controller driver self-test/example \
(baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI IIC (I2C) master/slave \
IP block in the embeddedsw stack. This recipe builds the driver \
self-test/example ELF that ships alongside the driver in the \
embeddedsw tree."
inherit esw_examples features_check
  
REQUIRED_MACHINE_FEATURES = "iic"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/iic/examples/"
