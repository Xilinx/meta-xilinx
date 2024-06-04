# Older xrt requires a manual dependency on libmetal
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'aie', ' libmetal', '', d)}"
