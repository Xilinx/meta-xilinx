inherit esw deploy python3native

ESW_COMPONENT_SRC = "/lib/sw_apps/freertos_hello_world/src/"

DEPENDS += "libxil xilstandalone freertos10-xilinx xiltimer"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 memory.ld ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/freertos_hello_world* ${D}/${base_libdir}/firmware
}

inherit image-artifact-names

FREERTOS_HELLO_WORLD_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {

    # We need to deploy the stripped elf, hence why not doing it from ${D}
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/freertos_hello_world.elf ${DEPLOYDIR}/${FREERTOS_HELLO_WORLD_BASE_NAME}.elf
    ln -sf ${FREERTOS_HELLO_WORLD_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf 
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/freertos_hello_world.elf ${WORKDIR}/package/${base_libdir}/firmware/freertos_hello_world.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/freertos_hello_world.bin ${DEPLOYDIR}/${FREERTOS_HELLO_WORLD_BASE_NAME}.bin
    ln -sf ${FREERTOS_HELLO_WORLD_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

FILES:${PN} = "${base_libdir}/firmware/freertos_hello_world*"
