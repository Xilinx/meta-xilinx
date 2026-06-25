DESCRIPTION = "RPU Core 1 Hello World Application"
SUMMARY = "Proxy recipe to fetch RPU1 hello-world firmware from baremetal multiconfig"

LICENSE = "CLOSED"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = ".*"
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RPU1_DEPENDS ??= ""
RPU1_MCDEPENDS ??= ""
RPU1_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
RPU1_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
RPU1_IMAGE_NAME ??= "hello-world-${MACHINE}"

RPU1_FILE ??= "${RPU1_DEPLOY_DIR}/${RPU1_IMAGE_NAME}"
RPU1_FILE[vardepsexclude] = "RPU1_DEPLOY_DIR"

do_fetch[depends] += "${RPU1_DEPENDS}"
do_fetch[mcdepends] += "${RPU1_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${RPU1_FILE}.elf ]; then
        echo "Unable to find RPU1_FILE (${RPU1_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${RPU1_FILE}.elf ${D}/boot/${PN}.elf
}

SHOULD_DEPLOY = "${@'false' if (d.getVar('RPU1_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${RPU1_FILE}.elf ${DEPLOYDIR}/${RPU1_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"

def check_rpu1_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('RPU1_DEPENDS') and not d.getVar('RPU1_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        if not os.path.exists(d.getVar('RPU1_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('RPU1_FILE')
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nEither specify RPU1_FILE, or enable the appropriate RPU1 baremetal multiconfig." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${RPU1_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${RPU1_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${RPU1_FILE}.elf:True')

python() {
    # Need to allow bbappends to change the check
    check_rpu1_vars(d)

    mcdepend = d.getVar('RPU1_MCDEPENDS')
    if mcdepend:
        if d.getVar('BB_CURRENT_MC') == 'default':
            d.setVar('RPU1_MCDEPENDS', mcdepend.replace('mc:default:', 'mc::'))
        else:
            d.setVar('RPU1_MCDEPENDS', mcdepend.replace('mc::', 'mc:${BB_CURRENT_MC}:'))
}
