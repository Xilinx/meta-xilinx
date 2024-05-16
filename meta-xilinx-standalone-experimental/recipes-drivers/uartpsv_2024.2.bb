inherit features_check

REQUIRED_MACHINE_FEATURES = "uartpsv"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartpsv/src/"
ESW_COMPONENT_NAME = "libuartpsv.a"

do_configure:prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} stdin
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 xuartpsv_g.c ${S}/${ESW_COMPONENT_SRC}/
}
