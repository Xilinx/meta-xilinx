include u-boot-xlnx.inc
include u-boot-spl-zynq-init.inc

XILINX_RELEASE_VERSION = "v2017.1"
SRCREV = "92e3dd638b50ad22dd90072673c80d8730903e95"
PV = "v2017.01-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"

SRC_URI_append = " \
		file://arm-zynqmp-xilinx_zynqmp.h-Auto-boot-in-JTAG-if-imag.patch \
		"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

# u-boot 2016.07 has support for these
HAS_PLATFORM_INIT ?= " \
		zynq_microzed_config \
		zynq_zed_config \
		zynq_zc702_config \
		zynq_zc706_config \
		zynq_zybo_config \
		xilinx_zynqmp_zcu102_config \
		"

