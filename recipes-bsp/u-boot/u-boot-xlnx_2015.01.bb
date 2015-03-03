include u-boot-xlnx.inc
include u-boot-elf.inc
include u-boot-extra.inc

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

# xilinx-v2014.1 release
SRCREV = "03464615e241054a38cd920980d6b12feba95585"
PV = "v2015.01${XILINX_EXTENSION}+git${SRCPV}"

UBOOT_ENV_zc702-zynq7 = "uEnv"
UBOOT_ENV_zedboard-zynq7 = "uEnv"

SRC_URI_append_zc702-zynq7 += "file://uEnv.txt"
SRC_URI_append_zedboard-zynq7 += "file://uEnv.txt"

do_compile_append() {
    # link u-boot-dtb.img to u-boot.img.
    if [ ! -e ${B}/u-boot-dtb.img ]; then
        ln -sf u-boot.img ${B}/u-boot-dtb.img
    fi
}

