SUMMARY = "AMD Xilinx Versal PDI parsing library (xilpdi)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
parses Versal Programmable Device Image (PDI) files used at boot."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilpdi/src/"
ESW_COMPONENT_NAME = "libxilpdi.a"

DEPENDS += "xilstandalone libxil"
