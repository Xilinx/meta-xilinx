
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-hdmitxss"

inherit esw python3native

DEPENDS += "xilstandalone v-hdmitx vtc v-hdmi-common"
DEPENDS += " ${@bb.utils.contains("MACHINE_FEATURES", "hdcp1x", "hdcp1x", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_hdmitxss/src/"
ESW_COMPONENT_NAME = "libv_hdmitxss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
