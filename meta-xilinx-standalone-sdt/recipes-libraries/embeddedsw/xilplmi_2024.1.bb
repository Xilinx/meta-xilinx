inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilplmi/src/"
ESW_COMPONENT_NAME = "libxilplmi.a"

DEPENDS += "xilstandalone libxil cfupmc xiltimer ${@'xilmailbox' if d.getVar('ESW_MACHINE') == 'psv_cortexa72_0' or d.getVar('ESW_MACHINE') == 'psv_cortexr5_0' else ''}"
