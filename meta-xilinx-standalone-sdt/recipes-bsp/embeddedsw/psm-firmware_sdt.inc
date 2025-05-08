inherit esw

DEPENDS += "xilstandalone libxil xiltimer"

ESW_COMPONENT_SRC = "/lib/sw_apps/versal_psmfw/src/"

do_configure:prepend() {
    (
    cd ${S}
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

do_install() {
    :
}

ESW_COMPONENT = "versal_psmfw.elf"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${PSM_FIRMWARE_BASE_NAME}.elf
    ln -sf ${PSM_FIRMWARE_BASE_NAME}.elf ${DEPLOYDIR}/${PSM_FIRMWARE_IMAGE_NAME}.elf
}

addtask deploy before do_build after do_install
