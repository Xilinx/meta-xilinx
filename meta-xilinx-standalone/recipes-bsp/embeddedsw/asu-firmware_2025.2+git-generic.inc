# Separate build directories for versal and versal-net
B = "${S}/lib/sw_apps/asufw/src"

BSP_DIR ?= "${B}/../../misc/versal_asu_bsp"

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/${PV}:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
    file://0001-makefile-Prevent-makefile-from-calling-copy_bsp.sh.patch\
    file://0001-versal_2ve_2vm_fw-Fixup-core-makefiles.patch \
    file://0001-Remove-hardcoded-multilibs.patch \
    "

EXTRA_COMPILER_FLAGS = "-Wall -Wextra"

do_configure() {
    # manually do the copy_bsp step first, so as to be able to fix up use of
    # mb-* commands
    ${B}/../misc/copy_bsp.sh
}

do_compile() {
    oe_runmake

    ${RISCV_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
