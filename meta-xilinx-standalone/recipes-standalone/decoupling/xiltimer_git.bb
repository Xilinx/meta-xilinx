inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xiltimer/src/"
ESW_COMPONENT_NAME = "libxiltimer.a"

DEPENDS += "dtc-native python3-pyyaml-native libxil device-tree"
