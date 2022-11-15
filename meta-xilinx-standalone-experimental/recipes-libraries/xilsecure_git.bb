inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilsecure/src/"
ESW_COMPONENT_NAME = "libxilsecure.a"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'ub1_cpu_pmc' else 'xilmailbox'}"
