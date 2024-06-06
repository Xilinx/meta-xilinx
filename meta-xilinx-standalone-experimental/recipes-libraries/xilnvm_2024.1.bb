inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilnvm/src/"
ESW_COMPONENT_NAME = "libxilnvm.a"

PACKAGECONFIG ??= "client server"
PACKAGECONFIG[client]  ="-DXILNVM_mode="client",,"
PACKAGECONFIG[server]  ="-DXILNVM_mode="server",,"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else 'xilmailbox'}"
