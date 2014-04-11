require linux-dtb.inc
require linux-machine-config.inc

# MicroBlaze is a uImage target, but its not called 'uImage'
DEPENDS_append_microblaze += "u-boot-mkimage-native"

# Common Device Tree Includes
MACHINE_DEVICETREE_append_zynq += " \
		common/zynq-7-base.dtsi \
		common/zynq-7-mainline-gem.dtsi \
		"

# Architecture specific kernel configuration parts, defconfigs for Xilinx architectures
MACHINE_KCONFIG_LINUX_VERSION ?= "${@'.'.join(d.getVar('LINUX_VERSION', True).split('.')[0:2])}"
MACHINE_KCONFIG_prepend_zynq += "common/linux/zynq/defconfig_${MACHINE_KCONFIG_LINUX_VERSION}.cfg "
MACHINE_KCONFIG_prepend_microblaze += "common/linux/microblaze/defconfig_${MACHINE_KCONFIG_LINUX_VERSION}.cfg "
