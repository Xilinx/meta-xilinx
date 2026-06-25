SUMMARY = "AMD Xilinx Versal PUF library (xilpuf)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
exposes the Versal Physically Unclonable Function (PUF) for key \
generation from baremetal firmware."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilpuf/src/"
ESW_COMPONENT_NAME = "libxilpuf.a"

PACKAGECONFIG ??= "client server"
PACKAGECONFIG[client]  ="-DXILPUF_Mode="client",,"
PACKAGECONFIG[server]  ="-DXILPUF_Mode="server",,"

DEPENDS += "\
    libxil \
    xiltimer \
    ${@'xilplmi' if d.getVar('ESW_MACHINE') in ['psv_pmc_0', 'pmc_0', 'psx_pmc_0'] else 'xilmailbox'} \
    "
