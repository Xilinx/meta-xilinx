require fsbl-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/2024.1:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://fsbl-fixups.patch \
           "

# This version does not build for zynq
COMPATIBLE_MACHINE:zynq = "none"
