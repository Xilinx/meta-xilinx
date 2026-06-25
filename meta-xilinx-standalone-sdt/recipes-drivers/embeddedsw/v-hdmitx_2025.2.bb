
inherit features_check

REQUIRED_MACHINE_FEATURES = "v-hdmitx"

inherit esw python3native

DEPENDS += "xilstandalone video-common v-hdmi-common"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_hdmitx/src/"
ESW_COMPONENT_NAME = "libv_hdmitx.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
