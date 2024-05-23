inherit esw python3native esw_apps_common

ESW_COMPONENT_SRC = "/lib/sw_apps/freertos_hello_world/src/"

DEPENDS += "libxil xilstandalone freertos10-xilinx xiltimer"

ESW_EXECUTABLE_NAME = "freertos_hello_world"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    cp -rf ${S}/scripts/linker_files/ ${S}/${ESW_COMPONENT_SRC}/linker_files
    )
}

FILES:${PN} = "${base_libdir}/firmware/freertos_hello_world*"
