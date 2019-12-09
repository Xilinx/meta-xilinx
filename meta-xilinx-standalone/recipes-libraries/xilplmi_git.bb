inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilplmi/src/"
ESW_COMPONENT_NAME = "libxilplmi.a"

DEPENDS += "xilstandalone libxil xilpm"

# Workaround for CR-1045055
do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${B}/${ESW_COMPONENT_NAME} ${D}${libdir}
}
