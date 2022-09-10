inherit esw python3native

ESW_COMPONENT_SRC = "/lib/bsp/standalone/src/"
ESW_COMPONENT_NAME = "libxilstandalone.a"

DEPENDS += "libgloss"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetal_bspconfig_xlnx ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 MemConfig.cmake ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.c ${S}/${ESW_COMPONENT_SRC}/common/
    )
}
