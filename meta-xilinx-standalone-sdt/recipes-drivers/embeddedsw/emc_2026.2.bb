
inherit features_check

REQUIRED_MACHINE_FEATURES = "emc"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/emc/src/"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

do_install() {
    install -d ${D}${includedir}
    cp -r ${B}/include/xemc.h ${D}${includedir}
}
