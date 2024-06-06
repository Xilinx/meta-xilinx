inherit esw python3native esw_apps_common

ESW_COMPONENT_SRC = "/lib/sw_apps/memory_tests/src/"

DEPENDS += "libxil xiltimer"

ESW_EXECUTABLE_NAME = "memory_tests"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} memtest
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    cp -rf ${S}/scripts/linker_files/ ${S}/${ESW_COMPONENT_SRC}/linker_files
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

FILES:${PN} = "${base_libdir}/firmware/memory_tests*"
