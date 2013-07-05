# Include path to xilinx-v14.5 modifications to u-boot 2013.01.01
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot:"
# Include path to patches to xilinx-v14.5
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"

SRC_URI += "file://xilinx-v2013.01/0001-Xilinx-modifications-to-arch.patch \
 file://xilinx-v2013.01/0002-Xilinx-modifications-to-boards.patch \
 file://xilinx-v2013.01/0003-Xilinx-modifications-to-commmon.patch \
 file://xilinx-v2013.01/0004-Xilinx-modifications-to-drivers.patch \
 file://xilinx-v2013.01/0005-Xilinx-modifications-to-configs.patch \
 file://microblaze_bootm_Add_support_for_loading_initrd.patch \
 file://microblaze_bootm_Fix_coding_style_issues.patch \
 file://microblaze_Fix_coding_style_for_bootb.patch \
 "
include u-boot-extra.inc
