
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-hdmitxss1"

inherit esw python3native

DEPENDS += "xilstandalone v-hdmitx1 vtc tmrctr v-hdmi-common"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "hdcp1x", "hdcp1x", "",d)}"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "hdcp22-tx", "hdcp22-tx", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_hdmitxss1/src/"
ESW_COMPONENT_NAME = "libv_hdmitxss1.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
