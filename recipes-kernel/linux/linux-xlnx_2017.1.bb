LINUX_VERSION = "4.9"
XILINX_RELEASE_VERSION = "v2017.1"
SRCREV ?= "68e6869cfb8154b80ee9ffafd64932971e9d1d07"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
	file://0003-drm-xilinx-Fix-DPMS-transition-to-on.patch \
	"

