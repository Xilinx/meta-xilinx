# We WANT to default to this version when available
DEFAULT_PREFERENCE = "100"

# Reset this
SRC_URI = "${EMBEDDEDSW_SRCURI}"

inherit esw

# Not compatible with Zynq
COMPATIBLE_MACHINE:zynq = "none"
COMPATIBLE_MACHINE:microblaze = "none"

ESW_COMPONENT_SRC = "/lib/sw_apps/undefined/src"
ESW_COMPONENT_SRC:zynq = "/lib/sw_apps/zynq_fsbl/src"
ESW_COMPONENT_SRC:zynqmp = "/lib/sw_apps/zynqmp_fsbl/src"

DEPENDS += "xilstandalone xiltimer xilffs xilsecure xilpm"

python() {
    psu_init_path = d.getVar('PSU_INIT_PATH')
    if not psu_init_path:
        psu_init_path = os.path.dirname(d.getVar('SYSTEM_DTFILE'))

    psu_init_c = os.path.join(psu_init_path, 'psu_init.c')
    psu_init_h = os.path.join(psu_init_path, 'psu_init.h')

    add_path = False
    if os.path.exists(psu_init_c):
        d.appendVar('SRC_URI', ' file://psu_init.c')
        add_path = True

    if os.path.exists(psu_init_h):
        d.appendVar('SRC_URI', ' file://psu_init.h')
        add_path = True

    if add_path:
        d.prependVar('FILESEXTRAPATHS', '%s:' % psu_init_path)
}

do_configure:prepend() {
    if [ -e ${UNPACKDIR}/psu_init.c ]; then
        install -m 0644 ${UNPACKDIR}/psu_init.c ${S}/${ESW_COMPONENT_SRC}
    else
        bbwarn "Using the default psu_init.c, this may not work correctly."
    fi

    if [ -e ${UNPACKDIR}/psu_init.h ]; then
        install -m 0644 ${UNPACKDIR}/psu_init.h ${S}/${ESW_COMPONENT_SRC}
    else
        bbwarn "Using the default psu_init.h, this may not work correctly."
    fi
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
}

do_install() {
    :
}

addtask deploy before do_build after do_package

ESW_COMPONENT = "undefined"
ESW_COMPONENT:zynq = "zynq_fsbl.elf"
ESW_COMPONENT:zynqmp = "zynqmp_fsbl.elf"

CFLAGS:append:aarch64 = " -DARMA53_64"
CFLAGS:append:armv7r = " -DARMR5"
