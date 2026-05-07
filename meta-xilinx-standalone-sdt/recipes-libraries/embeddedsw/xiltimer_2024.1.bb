SUMMARY = "AMD Xilinx generic timer abstraction library (xiltimer)."
DESCRIPTION = "Generic timer abstraction library from the AMD Xilinx \
embeddedsw tree that selects the appropriate hardware timer (TTC, \
scutimer, axi-timer, etc.) for use by baremetal firmware."
inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xiltimer/src/"
ESW_COMPONENT_NAME = "libxiltimer.a"

DEPENDS += "libxil"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}
