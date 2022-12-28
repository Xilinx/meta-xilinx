inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilpuf/src/"
ESW_COMPONENT_NAME = "libxilpuf.a"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else 'xilmailbox'}"
