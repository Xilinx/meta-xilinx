inherit features_check

REQUIRED_MACHINE_FEATURES = "common"

inherit esw python3native

DEPENDS += "xilstandalone "

PACKAGECONFIG ?= "${@bb.utils.contains("MACHINE_FEATURES", "clockps", "clockps", "", d)} \
		  ${@bb.utils.contains("MACHINE_FEATURES", "scugic", "scugic", "", d)} \
		  ${@bb.utils.contains("MACHINE_FEATURES", "intc", "intc", "", d)}"
PACKAGECONFIG[clockps] = "${RECIPE_SYSROOT}/usr/lib/libclockps.a,,clockps,,"
PACKAGECONFIG[scugic] = "${RECIPE_SYSROOT}/usr/lib/libscugic.a,,scugic,,"
PACKAGECONFIG[intc] = "${RECIPE_SYSROOT}/usr/lib/libintc.a,,intc,,"

ESW_COMPONENT_SRC = "/lib/bsp/standalone/src/common/intr/"
ESW_COMPONENT_NAME = "libcommon.a"

do_configure:prepend() {
    (
    cd ${S}/${ESW_COMPONENT_SRC}/
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/XilinxProcessorIPLib/drivers/intc/src/
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/XilinxProcessorIPLib/drivers/scugic/src/
    )
}
