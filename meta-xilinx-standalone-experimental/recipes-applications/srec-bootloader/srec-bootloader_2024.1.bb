inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/srec_bootloader/src/"

DEPENDS += "libxil xiltimer"

inherit python3native

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    cp -rf ${S}/scripts/linker_files/ ${S}/${ESW_COMPONENT_SRC}/linker_files
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/srec_bootloader* ${D}/${base_libdir}/firmware
}

inherit image-artifact-names

SREC_BOOTLOADER_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_CUSTOM_LINKER_FILE ?= "None"
EXTRA_OECMAKE = "-DCUSTOM_LINKER_FILE=${@d.getVar('ESW_CUSTOM_LINKER_FILE')}"

do_deploy() {

    # We need to deploy the stripped elf, hence why not doing it from ${D}
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/srec_bootloader.elf ${DEPLOYDIR}/${SREC_BOOTLOADER_BASE_NAME}.elf
    ln -sf ${SREC_BOOTLOADER_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/srec_bootloader.elf ${WORKDIR}/package/${base_libdir}/firmware/srec_bootloader.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/srec_bootloader.bin ${DEPLOYDIR}/${SREC_BOOTLOADER_BASE_NAME}.bin
    ln -sf ${SREC_BOOTLOADER_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

FILES:${PN} = "${base_libdir}/firmware/srec_bootloader*"
