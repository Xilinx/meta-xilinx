# linux-xlnx specific modifications for Xilinx KC705 TRD

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI_append_kc705-trd += "file://defconfig"

SRC_URI_append_kc705-trd += "file://kc705-trd.dts"
KERNEL_DEVICETREE_kc705-trd = "${WORKDIR}/kc705-trd.dts"

COMPATIBLE_MACHINE_kc705-trd = "kc705-trd"
