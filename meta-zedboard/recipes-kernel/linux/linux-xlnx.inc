# linux-xlnx specific modifications for zedboard

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI_append_zedboard += "file://nortc.cfg"

KERNEL_DEVICETREE_zedboard = "${S}/arch/arm/boot/dts/zynq-zed.dts"

COMPATIBLE_MACHINE_zedboard = "zedboard"
