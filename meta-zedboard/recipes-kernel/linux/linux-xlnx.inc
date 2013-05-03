# linux-xlnx specific modifications for zedboard

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

# Config fragments are either under 
# meta-xilinx/kernel-recipes for common fragments, or
# meta-xilinx/meta-<boardlayer>/kernel-recipes for board specific fragments
SRC_URI += "file://xilinx_zynq_defconfig.cfg"
SRC_URI_append_zedboard += "file://nortc.cfg"

KERNEL_DEVICETREE_zedboard = "${S}/arch/arm/boot/dts/zynq-zed.dts"

COMPATIBLE_MACHINE_zedboard = "zedboard"
