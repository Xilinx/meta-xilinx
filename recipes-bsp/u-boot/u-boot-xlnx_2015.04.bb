include u-boot-xlnx.inc
include u-boot-extra.inc
include u-boot-spl-zynq-init.inc

SRCREV = "1160fbcc619f23bf87cde01ed651566474e17eb9"
PV = "v2015.04${XILINX_EXTENSION}+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
SRC_URI += " \
		file://zynq-Add-Zynq-PicoZed-board-support.patch \
		file://microblaze-Fix-EMAC-Lite-initialization.patch \
		file://microblaze-generic_defconfig-Disable-configs.patch \
		file://0001-microblaze-Fix-style-issues-in-header-files.patch \
		file://0002-microblaze-Fix-C99-gnu99-compatiblity-for-inline-fun.patch \
		"

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 = " file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 = " file://uEnv.txt"

# 2015.04 - does not split the zc702 and zc706 into two configs
UBOOT_MACHINE_zc702-zynq7 = "zynq_zc70x_config"
UBOOT_MACHINE_zc706-zynq7 = "zynq_zc70x_config"

do_compile_append() {
    # link u-boot-dtb.img to u-boot.img.
    if [ ! -e ${B}/u-boot-dtb.img ]; then
        ln -sf u-boot.img ${B}/u-boot-dtb.img
    fi
}

