inherit features_check

REQUIRED_MACHINE_FEATURES = "asu-ram-ecc"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/asu_ram_ecc/src/"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

# XilinxProcessorIPLib/drivers/asu_ram_ecc/src/CMakeLists.txt only does
# add_library(asu_ram_ecc INTERFACE), so the build produces neither
# libasu_ram_ecc.a nor any header.  Override the esw.bbclass do_install,
# which would otherwise try to install ${B}/${ESW_COMPONENT_NAME}.
do_install() {
    :
}
