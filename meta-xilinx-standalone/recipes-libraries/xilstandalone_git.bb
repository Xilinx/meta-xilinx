inherit esw python3native

ESW_COMPONENT_SRC = "/lib/bsp/standalone/src/"
ESW_COMPONENT_NAME = "libxilstandalone.a"

DEPENDS += "dtc-native python3-pyyaml-native libgloss device-tree"

do_generate_bspcfg_data () {
    # This script should also not rely on relative paths and such
    cd ${S}
    nativepython3 ${S}/scripts/generate_libdata.py -d ${DTBFILE}
}

# Task dependencies might need to be fixed after unifying the DTB flow
do_create_dtb(){
    :
}

addtask do_generate_bspcfg_data before do_configure after do_create_dtb

addtask do_create_dtb before do_configure after do_prepare_recipe_sysroot
