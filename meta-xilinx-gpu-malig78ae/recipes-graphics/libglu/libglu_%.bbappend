# See meta-xilinx-mali400 for related settings
DEPENDS:append =  "${@bb.utils.contains('MACHINE_FEATURES', 'malig78ae', ' ${MALI_DEPENDS}', '', d)}"
