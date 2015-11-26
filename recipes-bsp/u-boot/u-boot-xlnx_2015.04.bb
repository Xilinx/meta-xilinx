include u-boot-xlnx.inc
include u-boot-xlnx-2015.04.inc
include u-boot-extra.inc

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

