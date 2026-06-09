SUMMARY = "AMD Zynq PS UART driver (baremetal)."
DESCRIPTION = "Baremetal driver for the AMD Zynq / Zynq UltraScale+ \
MPSoC PS-side Cadence UART controller in the embeddedsw stack."
inherit features_check

REQUIRED_MACHINE_FEATURES = "uartps"

inherit esw python3native

DEPENDS += "xilstandalone "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/uartps/src/"
ESW_COMPONENT_NAME = "libuartps.a"

do_configure:prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetalconfig_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} stdin
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 xuartps_g.c ${S}/${ESW_COMPONENT_SRC}/
}
