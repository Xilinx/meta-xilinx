inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilpm/src/"
ESW_COMPONENT_NAME = "libxilpm.a"

DEPENDS += "libxil ${@'xilplmi cframe' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else ''}"

def lopper_args(d):
     lopper_cmd_append = ""
     flags = [
         "APU_AS_OVERLAY_CONFIG_MASTER",
         "APU_AS_POWER_MANAGEMENT_MASTER",
         "APU_AS_RESET_MANAGEMENT_MASTER",
         "RPU0_AS_OVERLAY_CONFIG_MASTER",
         "RPU0_AS_POWER_MANAGEMENT_MASTER",
         "RPU0_AS_RESET_MANAGEMENT_MASTER",
         "RPU1_AS_OVERLAY_CONFIG_MASTER",
         "RPU1_AS_POWER_MANAGEMENT_MASTER",
         "RPU1_AS_RESET_MANAGEMENT_MASTER"
     ]

     for flag in flags:
         value = d.getVar("XILPM_" + flag)
         if value:
             lopper_cmd_append += "XILPM_" + flag.lower() + ":" + value.lower() + " "

     return lopper_cmd_append

LOPPER_CMD_APPEND = "${@lopper_args(d)}"

do_configure:prepend:zynqmp() {
    # This script should also not rely on relative paths and such
    (
        cd ${S}
        lopper -f --enhanced --werror ${DTS_FILE} -- generate_config_object pm_cfg_obj.c ${ESW_MACHINE} ${LOPPER_CMD_APPEND}
        install -m 0755 pm_cfg_obj.c ${S}/${ESW_COMPONENT_SRC}/zynqmp/client/common/
    )
}
