include u-boot-xlnx.inc
include u-boot-spl-zynq-init.inc

# this matches u-boot-xlnx 'xilinx-v2016.?' release tag
SRCREV = "68b454fbd9f7ce10e87e736888ea54c1655ce025"
PV = "v2016.01${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"

SRC_URI_append_kc705-trd-microblazeel = " file://microblaze-kc705-trd-Convert-microblaze-generic-to-k.patch"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 = " file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 = " file://uEnv.txt"

# u-boot 2016.01 has support for these
HAS_PS7INIT ?= " \
		zynq_microzed_config \
		zynq_zed_config \
		zynq_zc702_config \
		zynq_zc706_config \
		zynq_zybo_config \
		"

