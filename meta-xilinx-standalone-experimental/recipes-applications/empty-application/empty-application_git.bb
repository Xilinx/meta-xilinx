inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/empty_application/src/"

DEPENDS += "libxil xiltimer"

inherit python3native

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 memory.ld ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    install -m 0644 ${CUSTOM_SRCFILE}/* ${S}/${ESW_COMPONENT_SRC}/
    )
}

CUSTOM_APP_IMAGE_NAME ??= "custom-application"

ESW_CUSTOM_LINKER_FILE ?= "None"
EXTRA_OECMAKE = "-DCUSTOM_LINKER_FILE=${@d.getVar('ESW_CUSTOM_LINKER_FILE')}"

inherit image-artifact-names

CUSTOM_APP_BASE_NAME ?= "${CUSTOM_APP_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "executable.elf"

do_compile:append() {
    ${OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/executable.bin
}

do_install() {
    :
}

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${CUSTOM_APP_BASE_NAME}.elf
    ln -sf ${CUSTOM_APP_BASE_NAME}.elf ${DEPLOYDIR}/${CUSTOM_APP_IMAGE_NAME}.elf
    install -m 0644 ${B}/executable.bin ${DEPLOYDIR}/${CUSTOM_APP_BASE_NAME}.bin
    ln -sf ${CUSTOM_APP_BASE_NAME}.bin ${DEPLOYDIR}/${CUSTOM_APP_IMAGE_NAME}.bin
}
addtask deploy before do_build after do_install
