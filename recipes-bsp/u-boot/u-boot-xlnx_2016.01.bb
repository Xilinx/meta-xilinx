include u-boot-xlnx.inc

# this matches u-boot-xlnx 'xilinx-v2016.03' release tag
SRCREV = "07b02489f2f11459bf5e402f34c5e84d20ebbbcf"
PV = "v2016.01${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"

SRC_URI_append_kc705-trd-microblazeel = " file://microblaze-kc705-trd-Convert-microblaze-generic-to-k.patch"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 = " file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 = " file://uEnv.txt"
