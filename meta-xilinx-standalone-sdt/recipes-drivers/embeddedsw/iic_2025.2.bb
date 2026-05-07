SUMMARY = "AMD AXI IIC (I2C) controller driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD AXI IIC (I2C) master/slave \
IP block in the embeddedsw stack."
inherit features_check
  
REQUIRED_MACHINE_FEATURES = "iic"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/iic/src/"
ESW_COMPONENT_NAME = "libiic.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
