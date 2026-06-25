inherit features_check esw python3native

REQUIRED_MACHINE_FEATURES = "visp-ss"

DEPENDS += "xilstandalone"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/visp_ss/src/"
ESW_COMPONENT_NAME = "libvisp_ss.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

# Bypass compilation if no .c files exist
do_compile() {
    echo "Skipping compilation for visp-ss (no .c files present)"
}

# Create a dummy library if required
do_install() {
    install -d ${D}${libdir}
    echo "Creating a dummy libvisp_ss.a"
    ${TARGET_PREFIX}ar rcs ${D}${libdir}/libvisp_ss.a
}

