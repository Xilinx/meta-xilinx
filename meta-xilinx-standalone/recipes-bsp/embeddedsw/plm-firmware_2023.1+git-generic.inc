# Separate build directories for versal and versal-net
SOC_DIR = "versal"
SOC_DIR:versal-net = "versal_net"
B = "${S}/lib/sw_apps/versal_plm/src/${SOC_DIR}"

BSP_DIR ?= "${B}/../../misc/versal_plm_bsp"

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/${PV}:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://0001-versal_fw-Fixup-core-makefiles.patch \
           "

EXTRA_COMPILER_FLAGS = "-g -ffunction-sections -fdata-sections -Wall -Wextra -Os -flto -ffat-lto-objects"

do_configure() {
    # manually do the copy_bsp step first, so as to be able to fix up use of
    # mb-* commands
    ${B}/../../misc/${SOC_DIR}/copy_bsp.sh
}

do_compile() {
    oe_runmake

    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
