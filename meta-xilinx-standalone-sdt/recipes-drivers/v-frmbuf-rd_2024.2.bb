inherit features_check

REQUIRED_MACHINE_FEATURES = "v-frmbuf-rd"

inherit esw python3native

DEPENDS += "xilstandalone video-common"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/v_frmbuf_rd/src/"
ESW_COMPONENT_NAME = "libv_frmbuf_rd.a"

do_configure:prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 xv_frmbufrd_g.c ${S}/${ESW_COMPONENT_SRC}/
}
