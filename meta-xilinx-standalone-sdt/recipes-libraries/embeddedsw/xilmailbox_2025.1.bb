inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilmailbox/src/"
ESW_COMPONENT_NAME = "libxilmailbox.a"

DEPENDS += "xilstandalone libxil xiltimer"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}
