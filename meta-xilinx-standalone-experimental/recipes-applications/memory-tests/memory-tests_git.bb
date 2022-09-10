inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/memory_tests/src/"

DEPENDS += "libxil xiltimer"

inherit python3native

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} memtest
    install -m 0755 memory.ld ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/memory_test* ${D}/${base_libdir}/firmware
}

inherit image-artifact-names

MEMORY_TESTS_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {

    # We need to deploy the stripped elf, hence why not doing it from ${D}
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/memory_test.elf ${DEPLOYDIR}/${MEMORY_TESTS_BASE_NAME}.elf
    ln -sf ${MEMORY_TESTS_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/memory_test.elf ${WORKDIR}/package/${base_libdir}/firmware/memory_test.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/memory_test.bin ${DEPLOYDIR}/${MEMORY_TESTS_BASE_NAME}.bin
    ln -sf ${MEMORY_TESTS_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

FILES:${PN} = "${base_libdir}/firmware/memory_test*"
