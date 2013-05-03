# linux-xlnx specific modifications for zc702

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

# Config fragments are either under 
# meta-xilinx/kernel-recipes for common fragments, or
# meta-xilinx/meta-<boardlayer>/kernel-recipes for board specific fragments
SRC_URI += "file://xilinx_zynq_defconfig.cfg"
SRC_URI_append_zedboard += "file://rtc.cfg"

KERNEL_DEVICETREE_zc702 = "${S}/arch/arm/boot/dts/zynq-zc702.dts"

COMPATIBLE_MACHINE_zc702 = "zc702"
