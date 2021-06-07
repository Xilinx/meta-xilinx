require fsbl-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://0001-zynqmp_pmufw-Fix-reset-ops-for-assert.patch \
            file://0001-zynqmp_pmufw-Correct-structure-header-of-PmResetOps.patch \
            file://0001-sw_apps-versal_plm-Changes-to-ensure-versionless-bui.patch \
            file://0001-versal_psmfw-misc-Update-makefile-for-version-less-b.patch \
            file://0001-versal_psmfw-misc-Update-mcpu-version-in-Makefile.patch \
            file://zynqmp_pmufw-fixup.patch \
            file://makefile-skip-copy_bsp.sh.patch \
            file://fsbl-fixups.patch \
           "

# This version does not build for zynq
COMPATIBLE_MACHINE_zynq = "none"
