SUMMARY = "AMD Xilinx inter-processor mailbox library (xilmailbox)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
provides an abstraction over the inter-processor mailbox / IPI \
hardware on Zynq UltraScale+ and Versal devices."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilmailbox/src/"
ESW_COMPONENT_NAME = "libxilmailbox.a"

DEPENDS += "xilstandalone libxil xiltimer"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}
