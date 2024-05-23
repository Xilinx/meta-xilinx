
# What version of ESW and related should we default to?
# Note, there is no 'esw' recipe, but we're going to use this for all
# of the related items in this layer.
PREFERRED_VERSION_esw ??= "${@(d.getVar('XILINX_RELEASE_VERSION') or 'undefined').replace('v', '')}"

PREFERRED_VERSION_plm-firmware  ??= "${PREFERRED_VERSION_esw}%"
PREFERRED_VERSION_psm-firmware  ??= "${PREFERRED_VERSION_esw}%"
PREFERRED_VERSION_pmu-firmware  ??= "${PREFERRED_VERSION_esw}%"
PREFERRED_VERSION_fsbl-firmware ??= "${PREFERRED_VERSION_esw}%"
