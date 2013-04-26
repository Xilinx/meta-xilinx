# linux-xlnx specific modifications for zedboard

#Consider providing a skeleton bsp setup?

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI_append_zedboard += "file://defconfig"

#SRC_URI_append_zedboard += "file://zedboard.dts"
#KERNEL_DEVICETREE_zedboard = "${WORKDIR}/zynq-zed.dts"
#KERNEL_DEVICETREE_zedboard = "${WORKDIR}/Avnet-Digilent-Avnet-Digilent-ZedBoard-14.4.dts"
KERNEL_DEVICETREE_zedboard = "${S}/arch/arm/boot/dts/zynq-zed.dts"

COMPATIBLE_MACHINE_zedboard = "zedboard"

#KERNEL_IMAGETYPE = "zImage"
