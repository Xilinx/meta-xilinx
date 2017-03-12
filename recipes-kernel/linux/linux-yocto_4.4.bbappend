
FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " \
		file://ARM-dts-zynq-Enable-USB-and-USB-PHY-for-ZYBO.patch \
		"

