# This include file sets up the xilinx config fragments, these
# fragments build on top of the base config infrastructure provided in
# the linux-yocto/kernel-yocto recipes and classes in core layer of OE.

FILESEXTRAPATHS_prepend := "${THISDIR}/config:"

SRC_URI_append = " \
		file://xilinx-common;type=kmeta;destsuffix=xilinx-common \
		file://xilinx-machine;type=kmeta;destsuffix=xilinx-machine \
		"

