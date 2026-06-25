SUMMARY = "AMD Xilinx eFUSE / BBRAM key programming library (xilskey)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
programs the secure key (eFUSE, BBRAM) registers on Zynq, Zynq \
UltraScale+ MPSoC and Versal devices from baremetal firmware."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilskey/src/"
ESW_COMPONENT_NAME = "libxilskey.a"

DEPENDS += "libxil"
