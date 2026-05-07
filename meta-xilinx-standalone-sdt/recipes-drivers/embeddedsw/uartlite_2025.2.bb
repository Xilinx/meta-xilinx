SUMMARY = "AMD UART Lite (XPS UART Lite) driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD UART Lite IP block in the \
embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "uartlite"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartlite/src/"
ESW_COMPONENT_NAME = "libuartlite.a"

do_configure:prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} stdin
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 xuartlite_g.c ${S}/${ESW_COMPONENT_SRC}
}
