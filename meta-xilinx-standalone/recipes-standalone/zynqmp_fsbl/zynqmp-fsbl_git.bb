inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_fsbl/src"

DEPENDS += " xilstandalone xilffs xilsecure xilpm"

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${WORKDIR}/build/zynqmp_fsbl* ${D}/${base_libdir}/firmware
}

ZYNQMP_FSBL_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
ZYNQMP_FSBL_BASE_NAME[vardepsexclude] = "DATETIME"

do_deploy() {

    # Not a huge fan of deploying from package but we want the stripped elf to be deployed.
    # We could, technically create another task that runs after do_install that strips it but it
    # seems unnecessarily convoluted, unless there's an objection on performing do_install we
    # should do it this way since it easier to keep up with changes in oe-core.

    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.elf ${DEPLOYDIR}/${ZYNQMP_FSBL_BASE_NAME}.elf
    ln -sf ${ZYNQMP_FSBL_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.elf ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.bin ${DEPLOYDIR}/${ZYNQMP_FSBL_BASE_NAME}.bin
    ln -sf ${ZYNQMP_FSBL_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

CFLAGS_append = " -DARMA53_64"

FILES_${PN} = "${base_libdir}/firmware/zynqmp_fsbl*"
