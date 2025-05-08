inherit esw

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_pmufw/src"

DEPENDS += "xilstandalone xiltimer xilfpga xilskey"

do_configure:prepend() {
    (
    cd ${S}
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

do_install() {
    :
}

ESW_COMPONENT = "zynqmp_pmufw.elf"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${PMU_FIRMWARE_BASE_NAME}.elf
    ln -sf ${PMU_FIRMWARE_BASE_NAME}.elf ${DEPLOYDIR}/${PMU_FIRMWARE_IMAGE_NAME}.elf
}

addtask deploy before do_build after do_install
