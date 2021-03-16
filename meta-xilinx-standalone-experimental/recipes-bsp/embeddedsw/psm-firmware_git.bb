inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/versal_psmfw/src/"

DEPENDS += "xilstandalone libxil xiltimer"

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/versal_psmfw* ${D}/${base_libdir}/firmware
}

PSM_FIRMWARE_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
PSM_FIRMWARE_BASE_NAME[vardepsexclude] = "DATETIME"

do_deploy() {

    # Not a huge fan of deploying from package but we want the stripped elf to be deployed.
    # We could, technically create another task that runs after do_install that strips it but it
    # seems unnecessarily convoluted, unless there's an objection on performing do_install we
    # should do it this way since it easier to keep up with changes in oe-core.

    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/versal_psmfw.elf ${DEPLOYDIR}/${PSM_FIRMWARE_BASE_NAME}.elf
    ln -sf ${PSM_FIRMWARE_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/versal_psmfw.elf ${WORKDIR}/package/${base_libdir}/firmware/versal_psmfw.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/versal_psmfw.bin ${DEPLOYDIR}/${PSM_FIRMWARE_BASE_NAME}.bin
    ln -sf ${PSM_FIRMWARE_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

FILES_${PN} = "${base_libdir}/firmware/psmfw*"
