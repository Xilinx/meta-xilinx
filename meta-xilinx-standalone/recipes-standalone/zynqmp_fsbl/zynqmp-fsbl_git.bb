inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_fsbl/src"

DEPENDS += " xilstandalone xilffs xilsecure xilpm"

do_install() {
    :
}

ZYNQMP_FSBL_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
ZYNQMP_FSBL_BASE_NAME[vardepsexclude] = "DATETIME"

do_deploy() {
    install -Dm 0644 ${WORKDIR}/build/zynqmp_fsbl ${DEPLOYDIR}/${ZYNQMP_FSBL_BASE_NAME}.elf
    ln -sf ${ZYNQMP_FSBL_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/build/zynqmp_fsbl ${WORKDIR}/build/zynqmp_fsbl.bin
    install -m 0644 ${WORKDIR}/build/zynqmp_fsbl.bin ${DEPLOYDIR}/${ZYNQMP_FSBL_BASE_NAME}.bin
    ln -sf ${ZYNQMP_FSBL_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_install

CFLAGS_append = " -DARMA53_64"
