SKIP_MSG = ""
SKIP_MSG:zynq = "Generic support for zynq is not available"
SKIP_RECIPE[fsbl-firmware] = "${SKIP_MSG}"

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/${PV}:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://fsbl-fixups.patch \
           "

