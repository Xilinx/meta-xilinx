SUMMARY = "FreeRTOS 10 port for AMD Xilinx Zynq / Zynq UltraScale+ / \
Versal."
DESCRIPTION = "AMD Xilinx port of FreeRTOS 10 for the Cortex-A9, \
Cortex-A53, Cortex-R5 and Cortex-R52 cores in Zynq, Zynq UltraScale+ \
MPSoC, Versal and Versal NET devices, packaged as a baremetal library."
inherit esw python3native

ESW_COMPONENT_SRC = "/ThirdParty/bsp/freertos10_xilinx/src/"
ESW_COMPONENT_NAME = "libfreertos.a"

DEPENDS += "libxil xilstandalone  xiltimer"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}
