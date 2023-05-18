# Only should be used for development
DEFAULT_PREFERENCE = "-1"

require pmu-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/2023.1:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://0001-zynqmp_pmufw-Fixup-core-makefiles.patch \
           "

EXTRA_COMPILER_FLAGS = "-ffunction-sections -fdata-sections -Wall -Wextra -Os -flto -ffat-lto-objects"

do_compile() {
    oe_runmake

    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
