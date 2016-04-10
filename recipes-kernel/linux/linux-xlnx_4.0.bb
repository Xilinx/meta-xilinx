LINUX_VERSION = "4.0"
# This points at the 'xilinx-v2015.4.01' tag
SRCREV ?= "468329e7fac2b22e76897fbd40532cc0884ded2a"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
"