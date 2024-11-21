
inherit features_check

REQUIRED_MACHINE_FEATURES = "hdcp1x"

inherit esw python3native

DEPENDS += "xilstandalone tmrctr"

DEPENDS += " ${@bb.utils.contains_any("MACHINE_FEATURES", "v-hdmirx1", "v-hdmirx1", "",d)}"
DEPENDS += " ${@bb.utils.contains_any("MACHINE_FEATURES", "v-hdmitx1", "v-hdmitx1", "",d)}"
DEPENDS += " ${@bb.utils.contains_any("MACHINE_FEATURES", "v-hdmirx", "v-hdmirx", "",d)}"
DEPENDS += " ${@bb.utils.contains_any("MACHINE_FEATURES", "v-hdmitx", "v-hdmitx", "",d)}"
DEPENDS += " ${@bb.utils.contains_any("MACHINE_FEATURES", "dp12", "dp12", "",d)}"
DEPENDS += " ${@bb.utils.contains_any("MACHINE_FEATURES", "dp14", "dp14", "",d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/hdcp1x/src/"
ESW_COMPONENT_NAME = "libhdcp1x.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
