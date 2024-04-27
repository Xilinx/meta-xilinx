inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilloader/src/"
ESW_COMPONENT_NAME = "libxilloader.a"

#DEPENDS += "xilstandalone libxil xilpdi xilffs xilsecure xilpuf xilplmi"
DEPENDS += "xilstandalone libxil xilpm xilpdi xilffs xilsecure xilpuf xilsem"

DEPENDS += "cframe"
