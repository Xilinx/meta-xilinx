require pmu-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://${ESW_VER}/makefile-skip-copy_bsp.sh.patch \
            file://0001-zynqmp_pmufw-Fixup-core-makefiles.patch \
            file://0002-Avoid-race-induced-build-failure.patch \
           "

EXTRA_COMPILER_FLAGS = "-ffunction-sections -fdata-sections -Wall -Wextra -Os -flto -ffat-lto-objects"

do_compile() {
    oe_runmake

    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
