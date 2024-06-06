inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilpuf/src/"
ESW_COMPONENT_NAME = "libxilpuf.a"

PACKAGECONFIG ??= "client server"
PACKAGECONFIG[client]  ="-DXILPUF_Mode="client",,"
PACKAGECONFIG[server]  ="-DXILPUF_Mode="server",,"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else 'xilmailbox'}"
