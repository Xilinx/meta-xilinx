SUMMARY = "AMD Xilinx TPM 2.0 wrapper library (xiltpm)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that wraps \
a TPM 2.0 stack for use by Versal baremetal firmware."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xiltpm/src/"
ESW_COMPONENT_NAME = "libxiltpm.a"

DEPENDS += " \
    xilsecure \
    "
