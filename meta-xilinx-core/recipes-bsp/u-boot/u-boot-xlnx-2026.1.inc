UBRANCH = "xlnx_rebase_v2026.01"

SRCREV = "f917b2f6d33b43e63a034c01db967db66860942a"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=4;md5=c5130931598a8ad21840e124ffe64ea0"

# u-boot-xlnx has support for these
HAS_PLATFORM_INIT ?= " \
		xilinx_zynqmp_virt_config \
		xilinx_zynq_virt_defconfig \
		xilinx_versal_vc_p_a2197_revA_x_prc_01_revA \
		"

DEPENDS += "bc-native dtc-native python3-setuptools-native gnutls-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-v2026.01:"
