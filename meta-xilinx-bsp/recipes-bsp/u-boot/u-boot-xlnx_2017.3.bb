UBOOT_VERSION = "v2017.01"
XILINX_RELEASE_VERSION = "v2017.3"
SRCREV ?= "da811c4511ef9caeb95f9a22fe49d38bd8e56ded"

include u-boot-xlnx.inc
include u-boot-spl-zynq-init.inc

SRC_URI_append = " \
		file://arm64-zynqmp-Setup-partid-for-QEMU-to-match-silicon.patch \
		"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

# u-boot-xlnx has support for these
HAS_PLATFORM_INIT ?= " \
		zynq_microzed_config \
		zynq_zed_config \
		zynq_zc702_config \
		zynq_zc706_config \
		zynq_zybo_config \
		xilinx_zynqmp_zcu102_rev1_0_config \
		"

