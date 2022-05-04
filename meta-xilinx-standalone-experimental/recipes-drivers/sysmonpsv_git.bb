inherit features_check

REQUIRED_DISTRO_FEATURES = "sysmonpsv"

inherit esw python3native

DEPENDS += "xilstandalone ${@'scugic' if d.getVar('ESW_MACHINE') != 'microblaze-plm' and d.getVar('ESW_MACHINE') != 'microblaze-psm' else ''}"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/sysmonpsv/src/"
ESW_COMPONENT_NAME = "libsysmonpsv.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
