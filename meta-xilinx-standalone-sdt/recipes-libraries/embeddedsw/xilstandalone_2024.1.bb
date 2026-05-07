SUMMARY = "AMD Xilinx Standalone (no-OS) BSP library."
DESCRIPTION = "Core 'standalone' (no-OS) Board Support Package library \
from the AMD Xilinx Embedded Software (embeddedsw) tree, providing C \
runtime, exception handling, basic I/O and platform initialisation for \
baremetal firmware."
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
    install -m 0755 *.c ${S}/${ESW_COMPONENT_SRC}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 StandaloneExample.cmake ${S}/${ESW_COMPONENT_SRC}/common/
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetal_xparameters_xlnx.py ${ESW_MACHINE} ${S}
    install -m 0755 xparameters.h ${S}/${ESW_COMPONENT_SRC}/common/
    )

}
