SUMMARY = "AMD Xilinx Application Security Unit firmware library \
(xilasu)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that runs \
on the Versal Series Gen 2 Application Security Unit (ASU) RISC-V \
processor."
inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilasu/src/"
ESW_COMPONENT_NAME = "libxilasu.a"

DEPENDS += " \
    xilstandalone \
    libxil \
    xilmailbox \
    "
