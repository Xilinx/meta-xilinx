LINUX_VERSION = "4.6"
XILINX_RELEASE_VERSION = "v2016.4"
SRCREV ?= "2762bc9163bb8576f63ff82801a65576f59e1e57"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
	file://0003-drm-xilinx-Fix-DPMS-transition-to-on.patch \
	"

