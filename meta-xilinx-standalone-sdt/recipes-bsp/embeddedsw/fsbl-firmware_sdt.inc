inherit esw

# Not compatible with Zynq
COMPATIBLE_MACHINE:microblaze = "none"

ESW_COMPONENT_SRC = "/lib/sw_apps/undefined/src"
ESW_COMPONENT_SRC:zynq = "/lib/sw_apps/zynq_fsbl/src"
ESW_COMPONENT_SRC:zynqmp = "/lib/sw_apps/zynqmp_fsbl/src"

ESW_FSBL_DEPENDS = "xilstandalone xiltimer xilffs"
ESW_FSBL_DEPENDS:append:zynq = " xilrsa"
ESW_FSBL_DEPENDS:append:zynqmp = " xilsecure xilpm"

DEPENDS += "${ESW_FSBL_DEPENDS}"

CFLAGS:append:aarch64 = " -DARMA53_64"
CFLAGS:append:armv7r = " -DARMR5"

FSBL_PS_INIT_C:zynq = "ps7_init.c"
FSBL_PS_INIT_C:zynqmp = "psu_init.c"
FSBL_PS_INIT_H:zynq = "ps7_init.h"
FSBL_PS_INIT_H:zynqmp = "psu_init.h"

python() {
    psu_init_path = d.getVar('PSU_INIT_PATH')
    if not psu_init_path:
        psu_init_path = os.path.dirname(d.getVar('SYSTEM_DTFILE'))

    ps_init_c = d.getVar('FSBL_PS_INIT_C')
    ps_init_h = d.getVar('FSBL_PS_INIT_H')

    add_path = False
    if os.path.exists(os.path.join(psu_init_path, ps_init_c)):
        d.appendVar('SRC_URI', ' file://${FSBL_PS_INIT_C}')
        add_path = True

    if os.path.exists(os.path.join(psu_init_path, ps_init_h)):
        d.appendVar('SRC_URI', ' file://${FSBL_PS_INIT_H}')
        add_path = True

    if add_path:
        d.prependVar('FILESEXTRAPATHS', '%s:' % psu_init_path)
}

do_configure:prepend() {
    if [ -e ${WORKDIR}/${FSBL_PS_INIT_C} ]; then
        install -m 0644 ${WORKDIR}/${FSBL_PS_INIT_C} ${S}/${ESW_COMPONENT_SRC}
    else
        bbwarn "Using the default ${FSBL_PS_INIT_C}, this may not work correctly."
    fi

    if [ -e ${WORKDIR}/${FSBL_PS_INIT_H} ]; then
        install -m 0644 ${WORKDIR}/${FSBL_PS_INIT_H} ${S}/${ESW_COMPONENT_SRC}
    else
        bbwarn "Using the default ${FSBL_PS_INIT_H}, this may not work correctly."
    fi
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
}

do_install() {
    :
}

PACKAGES = ""

ESW_COMPONENT = "undefined"
ESW_COMPONENT:zynq = "zynq_fsbl.elf"
ESW_COMPONENT:zynqmp = "zynqmp_fsbl.elf"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${FSBL_BASE_NAME}.elf
    ln -sf ${FSBL_BASE_NAME}.elf ${DEPLOYDIR}/${FSBL_IMAGE_NAME}.elf
}

addtask deploy before do_build after do_package
