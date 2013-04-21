# linux-xlnx specific modifications for zedboard

#Consider providing a skeleton bsp setup?
#FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
#SRC_URI_append_zedboard += "file://zedboard.dts"
#KERNEL_DEVICETREE_zedboard = "${WORKDIR}/zedboard.dts"

KERNEL_DEVICETREE_zedboard = "${S}/arch/arm/boot/dts/zedboard.dts"

COMPATIBLE_MACHINE_zedboard = "zedboard"
