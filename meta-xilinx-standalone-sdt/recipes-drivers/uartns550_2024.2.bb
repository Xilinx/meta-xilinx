inherit features_check
  
REQUIRED_MACHINE_FEATURES = "uartns550"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartns550/src/"
ESW_COMPONENT_NAME = "libuartns550.a"

do_configure:prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} stdin
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 xuartns550_g.c ${S}/${ESW_COMPONENT_SRC}
}
