inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xiltimer/src/"
ESW_COMPONENT_NAME = "libxiltimer.a"

DEPENDS += "libxil"

do_configure_prepend() {
    # This script should also not rely on relative paths and such
    cd ${S}
    lopper.py ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
}
