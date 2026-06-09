
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-hdmi-common"

inherit esw python3native

DEPENDS += "xilstandalone video-common"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_hdmi_common/src/"
ESW_COMPONENT_NAME = "libv_hdmi_common.a"
