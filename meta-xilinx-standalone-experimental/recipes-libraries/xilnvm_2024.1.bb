inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilnvm/src/"
ESW_COMPONENT_NAME = "libxilnvm.a"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else 'xilmailbox'}"
