# linux-xlnx specific modifications for zc702

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

# Config fragments are either under 
# meta-xilinx/kernel-recipes for common fragments, or
# meta-xilinx/meta-<boardlayer>/kernel-recipes for board specific fragments
SRC_URI_append_zc702 += "file://xilinx_zynq_defconfig_${LINUX_VERSION}.cfg"
SRC_URI_append_zc702 += "file://rtc_${LINUX_VERSION}.cfg"
SRC_URI_append_zc702 += "file://zynq-zc702.dts"

KERNEL_DEVICETREE_zc702 = "${WORKDIR}/zynq-zc702.dts"

COMPATIBLE_MACHINE_zc702 = "zc702"
