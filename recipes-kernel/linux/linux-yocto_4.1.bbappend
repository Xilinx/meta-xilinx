
FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " \
		file://ARM-dts-zynq-Add-label-for-amba-node.patch \
		"

