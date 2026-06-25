SUMMARY = "AMD Xilinx eFUSE access library (xilnvm)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
reads/writes the non-volatile-memory (eFUSE) blocks of Versal devices \
from baremetal firmware."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilnvm/src/"
ESW_COMPONENT_NAME = "libxilnvm.a"

PACKAGECONFIG ??= "client server"
PACKAGECONFIG[client]  ="-DXILNVM_mode="client",,"
PACKAGECONFIG[server]  ="-DXILNVM_mode="server",,"

DEPENDS += "\
    libxil \
    xiltimer \
    ${@'xilplmi' if d.getVar('ESW_MACHINE') in ['psv_pmc_0', 'pmc_0', 'psx_pmc_0'] else 'xilmailbox'} \
    "
