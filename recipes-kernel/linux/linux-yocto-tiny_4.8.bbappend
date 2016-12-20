
require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " \
		file://ARM-zynq-Reserve-correct-amount-of-non-DMA-RAM.patch \
		"

