inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilsecure/src/"
ESW_COMPONENT_NAME = "libxilsecure.a"

PACKAGECONFIG ??= "client server"
PACKAGECONFIG[client]  ="-DXILSECURE_mode="client",,"
PACKAGECONFIG[server]  ="-DXILSECURE_mode="server",,"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' or d.getVar('ESW_MACHINE') == 'psv_cortexa72_0' or d.getVar('ESW_MACHINE') == 'psv_cortexr5_0' else ''} ${@'xilmailbox' if d.getVar('ESW_MACHINE') == 'psv_cortexa72_0' or d.getVar('ESW_MACHINE') == 'psv_cortexr5_0' else ''}"

do_install:append() {
    install -m 0755  ${B}/*.a ${D}${libdir}
}
