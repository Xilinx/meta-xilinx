inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "xdmapcie"

DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'gpiops', 'gpiops', '', d)}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/xdmapcie/examples/"
