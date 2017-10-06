include u-boot-xlnx.inc

XILINX_RELEASE_VERSION = "v2017.4"
SRCREV ?= "5fa7d2ed066166571e969d036c1871c1759a921d"
PV = "v2017.01-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot:"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 = " file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 = " file://uEnv.txt"
SRC_URI_append_kc705-microblazeel = " file://microblaze-kc705-Convert-microblaze-generic-to-k.patch"
