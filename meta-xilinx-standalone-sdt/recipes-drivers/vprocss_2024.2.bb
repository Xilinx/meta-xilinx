inherit features_check

REQUIRED_MACHINE_FEATURES = "vprocss"

inherit esw python3native

DEPENDS += "xilstandalone  video-common gpio axis-switch axivdma v-csc v-deinterlacer v-hcresampler v-vcresampler v-hscaler v-vscaler v-letterbox"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/vprocss/src/"
ESW_COMPONENT_NAME = "libvprocss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
