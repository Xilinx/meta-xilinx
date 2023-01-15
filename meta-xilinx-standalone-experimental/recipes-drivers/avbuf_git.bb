inherit features_check

REQUIRED_MACHINE_FEATURES = "avbuf"

inherit esw

DEPENDS += "xilstandalone"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/avbuf/src/"
ESW_COMPONENT_NAME = "libavbuf.a"
