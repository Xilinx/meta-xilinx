inherit esw python3native

ESW_COMPONENT_SRC = "/lib/bsp/standalone/src/"
ESW_COMPONENT_NAME = "libxilstandalone.a"

DEPENDS += "libgloss"

S = "${B}"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    lopper ${DTS_FILE} -- baremetal_bspconfig_xlnx ${ESW_MACHINE} ${B}/${ESW_COMPONENT_SRC}
    install -m 0755 MemConfig.cmake ${B}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.c ${B}/${ESW_COMPONENT_SRC}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx ${ESW_MACHINE} ${B}/${ESW_COMPONENT_SRC} hwcmake_metadata ${B}
    install -m 0755 StandaloneExample.cmake ${B}/${ESW_COMPONENT_SRC}/common/
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetal_xparameters_xlnx.py ${ESW_MACHINE} ${B}
    install -m 0755 xparameters.h ${B}/${ESW_COMPONENT_SRC}/common/

}
