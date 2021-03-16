inherit features_check

REQUIRED_DISTRO_FEATURES = "avbuf"

inherit esw

DEPENDS += "xilstandalone xilmem"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/avbuf/src/"
ESW_COMPONENT_NAME = "libavbuf.a"
