# Reset this
SRC_URI = "${EMBEDDEDSW_SRCURI}"
SRC_URI += "file://0001-zynqmp_pmufw-Fix-reset-ops-for-assert.patch"
SRC_URI += "file://0001-zynqmp_pmufw-Correct-structure-header-of-PmResetOps.patch"

inherit esw

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_pmufw/src"

ESW_COMPONENT = "pmufw.elf"

do_compile_append() {
    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/executable.bin
}

do_install() {
    :
}

DEPENDS += "xilstandalone xiltimer xilfpga xilskey"
