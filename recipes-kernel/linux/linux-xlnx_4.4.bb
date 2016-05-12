LINUX_VERSION = "4.4"
# This points to released tag xilinx-v2016.1.01
SRCREV ?="89cc643affcce18122373fe7c78e780526243fdf"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
	file://drm-xilinx-Fix-DPMS-transition-to-on.patch \
	"
