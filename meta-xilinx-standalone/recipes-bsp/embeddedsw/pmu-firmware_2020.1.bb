require pmu-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://0001-zynqmp_pmufw-Fix-reset-ops-for-assert.patch \
            file://0001-zynqmp_pmufw-Correct-structure-header-of-PmResetOps.patch \
           "

