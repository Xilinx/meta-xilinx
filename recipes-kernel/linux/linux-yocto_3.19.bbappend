
require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx/3.19:"
SRC_URI_append_zynq += " \
		file://tty-xuartps-Fix-RX-hang-and-TX-corruption-in-termios.patch \
		"

