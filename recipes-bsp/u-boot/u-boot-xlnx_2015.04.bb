include u-boot-xlnx.inc
include u-boot-xlnx-2015.04.inc
include u-boot-extra.inc

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

