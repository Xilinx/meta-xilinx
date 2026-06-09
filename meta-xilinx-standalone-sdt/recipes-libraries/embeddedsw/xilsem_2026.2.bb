SUMMARY = "AMD Xilinx Soft Error Mitigation library (xilsem)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
exposes the Versal SEM (Soft Error Mitigation) IP for fabric error \
scrubbing."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilsem/src/"
ESW_COMPONENT_NAME = "libxilsem.a"

DEPENDS += "xilstandalone libxil"
