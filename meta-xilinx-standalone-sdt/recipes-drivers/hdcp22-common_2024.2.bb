
inherit features_check

REQUIRED_MACHINE_FEATURES = "hdcp22-common"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/hdcp22_common/src/"
ESW_COMPONENT_NAME = "libhdcp22_common.a"

