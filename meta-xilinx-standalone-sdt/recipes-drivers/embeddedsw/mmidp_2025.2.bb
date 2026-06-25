inherit features_check

REQUIRED_MACHINE_FEATURES = "mmidp"

inherit esw python3native

DEPENDS += "\
    xilstandalone \
    dcsub \
    video-common \
    "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/mmidp/src/"
ESW_COMPONENT_NAME = "libmmidp.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
