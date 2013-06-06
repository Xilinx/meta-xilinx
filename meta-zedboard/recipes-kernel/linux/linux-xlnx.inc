# linux-xlnx specific modifications for zedboard

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

# Config fragments are either under 
# meta-xilinx/kernel-recipes for common fragments, or
# meta-xilinx/meta-<boardlayer>/kernel-recipes for board specific fragments
SRC_URI_append_zedboard += "file://xilinx_zynq_defconfig_${LINUX_VERSION}.cfg"
SRC_URI_append_zedboard += "file://nortc.cfg"
SRC_URI_append_zedboard += "file://zynq-zed-mmcblk0p2.dts"
SRC_URI_append_zedboard += "file://zynq-zed-ram.dts"

KERNEL_DEVICETREE_zedboard = "${WORKDIR}/zynq-zed-mmcblk0p2.dts ${WORKDIR}/zynq-zed-ram.dts"

COMPATIBLE_MACHINE_zedboard = "zedboard"
