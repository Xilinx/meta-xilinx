inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilpm/src/"
ESW_COMPONENT_NAME = "libxilpm.a"

DEPENDS = "libxil ${@'xilplmi cframe' if d.getVar('ESW_MACHINE') == 'ub1_cpu_pmc' else ''}"
