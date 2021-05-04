# Only should be used for development
DEFAULT_PREFERENCE = "-1"

require fsbl-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://fsbl-fixups.patch \
           "

# This version does not build for zynq
COMPATIBLE_MACHINE_zynq = "none"
