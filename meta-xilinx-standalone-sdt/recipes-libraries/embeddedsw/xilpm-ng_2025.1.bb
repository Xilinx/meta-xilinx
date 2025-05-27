inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilpm_ng/src/"
ESW_COMPONENT_NAME = "libxilpm_ng.a"

EXTRA_OECMAKE += "-DXILPM_NG_EEMI_ENABLE=ON"

DEPENDS += " \
    libxil \
    xilstandalone \
    ${@'xilplmi cframe' if d.getVar('ESW_MACHINE') == 'pmc_0' else ''} \
    "
