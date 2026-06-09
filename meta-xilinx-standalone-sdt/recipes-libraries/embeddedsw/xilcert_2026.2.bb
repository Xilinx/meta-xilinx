SUMMARY = "AMD Xilinx X.509 certificate library (xilcert)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
parses and verifies X.509 certificates for the Versal secure-boot \
flow."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilcert/src/"
ESW_COMPONENT_NAME = "libxilcert.a"

DEPENDS += " \
    xilstandalone \
    libxil \
    xilsecure \
    ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'psx_pmc_0' \
        or d.getVar('ESW_MACHINE') == 'pmc_0' else ''} \
    "
