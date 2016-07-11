LINUX_VERSION = "4.4"
# This points to released tag xilinx-v2016.2
SRCREV ?="dfb97bc345e323f2851022be24b0846183d158cc"

include linux-xlnx.inc

SRC_URI_append_zybo-linux-bd-zynq7 = " \
	file://0001-drm-xilinx-Add-encoder-for-Digilent-boards.patch \
	file://0002-clk-Add-driver-for-axi_dynclk-IP-Core.patch \
	file://drm-xilinx-Fix-DPMS-transition-to-on.patch \
	"
