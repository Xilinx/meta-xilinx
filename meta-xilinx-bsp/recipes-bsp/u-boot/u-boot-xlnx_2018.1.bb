UBOOT_VERSION = "v2018.01"
XILINX_RELEASE_VERSION = "v2018.1"

UBRANCH ?= "master"

SRCREV ?= "1c81b42a326e5b74a5b79e55de9c52b5781b7a8a"

include u-boot-xlnx.inc
include u-boot-spl-zynq-init.inc

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

