
require linux-machine-config.inc

# MicroBlaze is a uImage target, but its not called 'uImage'
DEPENDS_append_microblaze += "u-boot-mkimage-native"

# Common Device Tree Includes
# (put them in ${WORKDIR} to avoid using dtc with include paths)
FILESEXTRAPATHS_append := "${XILINX_LAYERDIR}/conf/machine/boards/common:"
SRC_URI_append += "file://zynq-7-base.dtsi"
