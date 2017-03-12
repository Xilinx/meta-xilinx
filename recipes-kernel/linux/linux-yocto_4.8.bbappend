
FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " \
		file://ARM-zynq-Reserve-correct-amount-of-non-DMA-RAM.patch \
		"

