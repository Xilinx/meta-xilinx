inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilasu/src/"
ESW_COMPONENT_NAME = "libxilasu.a"

DEPENDS += " \
    xilstandalone \
    libxil \
    xilmailbox \
    "
