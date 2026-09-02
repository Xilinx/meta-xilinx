inherit features_check

REQUIRED_MACHINE_FEATURES = "asu-ram-ecc"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/asu_ram_ecc/src/"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

# asu_ram_ecc is a config-only driver (INTERFACE library), so the build
# produces no libasu_ram_ecc.a - only xasu_ram_ecc.h.  Override the
# esw.bbclass do_install, which would otherwise try to install
# ${B}/${ESW_COMPONENT_NAME}, and install just the header.
do_install() {
    install -d ${D}${includedir}
    cp -r ${B}/include/xasu_ram_ecc.h ${D}${includedir}
}
