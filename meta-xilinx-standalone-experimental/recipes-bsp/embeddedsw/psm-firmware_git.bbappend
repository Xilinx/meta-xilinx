# Reset this
SRC_URI = "${EMBEDDEDSW_SRCURI}"

inherit esw

ESW_COMPONENT_SRC = "/lib/sw_apps/versal_psmfw/src/"

ESW_COMPONENT = "versal_psmfw.elf"

do_compile_append() {
    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/psmfw.bin
}

do_install() {
    :
}

DEPENDS += "xilstandalone libxil xiltimer"
