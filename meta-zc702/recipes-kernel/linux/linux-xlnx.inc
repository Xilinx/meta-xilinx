# linux-xlnx specific modifications for zc702

#Consider providing a skeleton bsp setup?

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI_append_zc702 += "file://defconfig"

#SRC_URI_append_zc702 += "file://zc702.dts"
#KERNEL_DEVICETREE_zc702 = "${WORKDIR}/zynq-zed.dts"
#KERNEL_DEVICETREE_zc702 = "${WORKDIR}/Avnet-Digilent-Avnet-Digilent-zc702-14.4.dts"
KERNEL_DEVICETREE_zc702 = "${S}/arch/arm/boot/dts/zynq-zc702.dts"

COMPATIBLE_MACHINE_zc702 = "zc702"
