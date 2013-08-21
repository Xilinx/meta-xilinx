require linux-dtb.inc
require linux-machine-config.inc

# MicroBlaze is a uImage target, but its not called 'uImage'
DEPENDS_append_microblaze += "u-boot-mkimage-native"

# Common Device Tree Includes
MACHINE_DEVICETREE_append_zynq += "common/zynq-7-base.dtsi"
