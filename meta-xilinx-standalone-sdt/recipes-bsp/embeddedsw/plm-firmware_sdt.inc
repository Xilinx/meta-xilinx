inherit esw

DEPENDS += "xilstandalone xiltimer xilffs xilpdi xilplmi xilloader xilpm xilsecure xilsem xilnvm"

ESW_COMPONENT_SRC = "/lib/sw_apps/versal_plm/src/"

do_configure:prepend() {
    (
    cd ${S}
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

do_compile:append() {
    ${OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}

do_install() {
    :
}

ESW_COMPONENT = "versal_plm.elf"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${PLM_BASE_NAME}.elf
    ln -sf ${PLM_BASE_NAME}.elf ${DEPLOYDIR}/${PLM_IMAGE_NAME}.elf
    install -m 0644 ${B}/${ESW_COMPONENT}.bin ${DEPLOYDIR}/${PLM_BASE_NAME}.bin
    ln -sf ${PLM_BASE_NAME}.bin ${DEPLOYDIR}/${PLM_IMAGE_NAME}.bin
}

addtask deploy before do_build after do_install
