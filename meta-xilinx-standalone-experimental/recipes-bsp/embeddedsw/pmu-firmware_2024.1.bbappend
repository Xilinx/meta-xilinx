# We WANT to default to this version when available 
DEFAULT_PREFERENCE = "100"

# Reset this
SRC_URI = "${EMBEDDEDSW_SRCURI}"

inherit esw

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_pmufw/src"

ESW_COMPONENT = "zynqmp_pmufw.elf"


do_configure:prepend() {
    (
    cd ${S}
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

do_install() {
    :
}

DEPENDS += "xilstandalone xiltimer xilfpga xilskey"
