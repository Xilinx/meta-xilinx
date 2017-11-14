LINUX_VERSION = "4.9"
XILINX_RELEASE_VERSION = "v2017.3"
SRCREV ?= "f1b1e077d641fc83b54c1b8f168cbb58044fbd4e"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
	file://0003-drm-xilinx-Fix-DPMS-transition-to-on.patch \
	"

