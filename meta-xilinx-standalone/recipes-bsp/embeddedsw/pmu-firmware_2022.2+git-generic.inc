FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/${PV}:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://0001-zynqmp_pmufw-Fixup-core-makefiles.patch \
           "

EXTRA_COMPILER_FLAGS = "-ffunction-sections -fdata-sections -Wall -Wextra -Os -flto -ffat-lto-objects"
