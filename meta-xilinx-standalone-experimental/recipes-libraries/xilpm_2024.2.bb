inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilpm/src/"
ESW_COMPONENT_NAME = "libxilpm.a"

DEPENDS += "libxil ${@'xilplmi cframe' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else ''}"

do_configure:prepend:zynqmp() {
    # This script should also not rely on relative paths and such
    (
        cd ${S}
        lopper -f --enhanced --werror ${DTS_FILE} -- generate_config_object pm_cfg_obj.c ${ESW_MACHINE}
        install -m 0755 pm_cfg_obj.c ${S}/${ESW_COMPONENT_SRC}/zynqmp/client/common/
    )
}
