inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilocp/src/"
ESW_COMPONENT_NAME = "libxilocp.a"

DEPENDS += " \
    xilstandalone \
    libxil \
    xilsecure \
    ${@'xilplmi xilcert' if d.getVar('ESW_MACHINE') == 'psx_pmc_0' \
        or d.getVar('ESW_MACHINE') == 'pmc_0' else ''} \
    ${@'xilmailbox' if d.getVar('ESW_MACHINE') == 'psx_cortexa78_0' \
        or d.getVar('ESW_MACHINE') == 'psx_cortexr52_0' \
        or d.getVar('ESW_MACHINE') == 'pmc_0' \
        or d.getVar('ESW_MACHINE') == 'psx_pmc_0' else ''} \
    "
