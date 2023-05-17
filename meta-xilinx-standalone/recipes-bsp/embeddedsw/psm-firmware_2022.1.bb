require psm-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://${ESW_VER}/makefile-skip-copy_bsp.sh.patch \
            file://0001-versal_fw-Fixup-core-makefiles.patch \
           "

EXTRA_COMPILER_FLAGS = "-g -ffunction-sections -fdata-sections -Wall -Wextra"

do_compile() {
    oe_runmake

    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
