inherit features_check

REQUIRED_DISTRO_FEATURES = "csudma"

inherit esw python3native

DEPENDS += "xilstandalone xilmem"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/csudma/src/"
ESW_COMPONENT_NAME = "libcsudma.a"

do_configure_prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py ${DTS_FILE} -- baremetal_xparameters_xlnx.py ${ESW_MACHINE} ${S}
    install -m 0755 xparameters.h ${S}/${ESW_COMPONENT_SRC}/
}

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
