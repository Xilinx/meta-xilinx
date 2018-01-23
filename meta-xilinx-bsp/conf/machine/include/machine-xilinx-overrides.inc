SOC_VARIANT ??= ""
MACHINEOVERRIDES =. "${@['', '${SOC_FAMILY}${SOC_VARIANT}:']['${SOC_VARIANT}' != '']}"

INHERIT += "machine-overrides-extender"

MACHINEOVERRIDES_EXTENDER_zynqmpeg = "mali400"
MACHINEOVERRIDES_EXTENDER_zynqmpev = "mali400:vcu"
