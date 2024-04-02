require pmu-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/2024.1:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://0001-zynqmp_pmufw-Fixup-core-makefiles.patch \
           "

EXTRA_COMPILER_FLAGS = "-ffunction-sections -fdata-sections -Wall -Wextra ${ESW_CFLAGS}"
