include u-boot-xlnx.inc
include u-boot-spl-zynq-init.inc

# this matches u-boot-xlnx 'xilinx-v2015.4' release tag
SRCREV = "4942ae4d03ee4ef4b2dd750d441f730150ee9288"
PV = "v2015.07${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
SRC_URI += " \
		file://0001-microblaze-Fix-style-issues-in-header-files.patch \
		file://0002-microblaze-Fix-C99-gnu99-compatiblity-for-inline-fun.patch \
		"

SRC_URI_append_kc705-trd-microblazeel = " file://microblaze-kc705-trd-Convert-microblaze-generic-to-k.patch"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 = " file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 = " file://uEnv.txt"

# u-boot 2015.07 has support for these
HAS_PS7INIT ?= " \
		zynq_microzed_config \
		zynq_zed_config \
		zynq_zc702_config \
		zynq_zc706_config \
		"

