inherit esw python3native

ESW_COMPONENT_SRC = "/ThirdParty/bsp/freertos10_xilinx/src/"
ESW_COMPONENT_NAME = "libfreertos.a"

DEPENDS += "libxil xilstandalone xilmem xiltimer"

do_configure_prepend() {
    # This script should also not rely on relative paths and such
    cd ${S}
    lopper.py ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
}
