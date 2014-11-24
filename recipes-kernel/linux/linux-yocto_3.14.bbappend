
require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx/3.14:"
SRC_URI_append_zynq += " \
		file://tty-xuartps-Fix-RX-hang-and-TX-corruption-in-set_termios.patch \
		"

