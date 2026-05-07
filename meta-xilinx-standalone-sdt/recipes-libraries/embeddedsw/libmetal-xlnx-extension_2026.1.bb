SUMMARY = "AMD Xilinx libmetal extensions for embeddedsw."
DESCRIPTION = "AMD Xilinx extensions to the libmetal hardware \
abstraction library, used by OpenAMP and other inter-processor \
communication stacks in the embeddedsw tree."
inherit esw python3native

ESW_COMPONENT_SRC = "/ThirdParty/sw_services/libmetal_xlnx_extension/src/"
ESW_COMPONENT_NAME = "libmetal_xlnx_extension.a"

DEPENDS += "libxil ipipsu xiltimer libmetal-xlnx"
DEPENDS:append:xilinx-freertos = "freertos10-xilinx"

do_install() {
    install -d ${D}${libdir}
    install -m 0755  ${B}/lib/${ESW_COMPONENT_NAME} ${D}${libdir}
}

# this is same for r5 and r52
EXTRA_OECMAKE = ""
EXTRA_OECMAKE:armv7r += " -DMACHINE=zynqmp_r5 "
EXTRA_OECMAKE:armv8r += " -DMACHINE=zynqmp_r5 "
