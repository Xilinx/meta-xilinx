# We WANT to default to this version when available 
DEFAULT_PREFERENCE = "100"

# Reset this
SRC_URI = "${EMBEDDEDSW_SRCURI}"

inherit esw

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_pmufw/src"

ESW_COMPONENT = "pmufw.elf"

do_compile:append() {
    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}

do_install() {
    :
}

DEPENDS += "xilstandalone xiltimer xilfpga xilskey"
