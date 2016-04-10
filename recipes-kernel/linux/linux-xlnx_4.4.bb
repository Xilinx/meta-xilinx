LINUX_VERSION = "4.4"
# This points to released tag xilinx-v2016.1
SRCREV ?="dd7c1f0b5c23bcac5046d77bd5e0631e657003a4"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
"