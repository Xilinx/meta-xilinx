inherit esw python3native esw_apps_common

ESW_COMPONENT_SRC = "/lib/sw_apps/lwip_udp_perf_client/src/"

DEPENDS += "libxil lwip xiltimer"

ESW_EXECUTABLE_NAME = "lwip_udp_perf_client"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    cp -rf ${S}/scripts/linker_files/ ${S}/${ESW_COMPONENT_SRC}/linker_files
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

do_generate_app_data() {
    # This script should also not rely on relative paths and such
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
}
addtask do_generate_app_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

FILES:${PN} = "${base_libdir}/firmware/lwip_udp_perf_client*"
