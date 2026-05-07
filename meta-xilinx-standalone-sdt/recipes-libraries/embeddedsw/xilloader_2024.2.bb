SUMMARY = "AMD Xilinx Versal PLM loader library (xilloader)."
DESCRIPTION = "Subsystem of the Versal Platform Loader and Manager \
(PLM) firmware that loads partitions from the boot Programmable Device \
Image (PDI), forming part of the AMD Xilinx embeddedsw stack."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilloader/src/"
ESW_COMPONENT_NAME = "libxilloader.a"

#DEPENDS += "xilstandalone libxil xilpdi xilffs xilsecure xilpuf xilplmi"
DEPENDS += "xilstandalone libxil xilpm xilpdi xilffs xilsecure xilpuf xilsem"

DEPENDS += "cframe"
