include u-boot-xlnx.inc
include u-boot-spl-zynq-init.inc

XILINX_RELEASE_VERSION = "v2016.3"
# this matches u-boot-xlnx 'xilinx-v2016.3' release tag
SRCREV = "95e11f6eb4bc07bbee12a4217c58146bddac61b2"
PV = "v2016.07-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"

SRC_URI_append = " file://0001-compiler-gcc6.h-Fix-u-boot-issue-for-gcc6.patch"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 = " file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 = " file://uEnv.txt"

# u-boot 2016.07 has support for these
HAS_PS7INIT ?= " \
		zynq_microzed_config \
		zynq_zed_config \
		zynq_zc702_config \
		zynq_zc706_config \
		zynq_zybo_config \
		"

