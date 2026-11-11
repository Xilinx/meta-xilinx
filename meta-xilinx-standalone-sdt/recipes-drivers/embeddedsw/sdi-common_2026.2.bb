
inherit features_check

REQUIRED_MACHINE_FEATURES = "sdi-common"

inherit esw python3native

DEPENDS += "xilstandalone video-common"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/sdi_common/src/"
ESW_COMPONENT_NAME = "libsdi_common.a"

