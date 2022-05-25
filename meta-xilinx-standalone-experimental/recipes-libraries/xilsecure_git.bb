inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilsecure/src/"
ESW_COMPONENT_NAME = "libxilsecure.a"

DEPENDS += "libxil xiltimer ${@'xilplmi' if d.getVar('ESW_MACHINE') == 'microblaze-plm' else 'xilmailbox'}"
