DESCRIPTION = "Application Security Unit"
SUMMARY = "Application Security Unit for Versal_2ve_2vm devices"

LICENSE = "CLOSED"

PROVIDES = "virtual/asu"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the asu-firmware in the deploy
# directory, named "asu-${MACHINE}.elf" and "asu-${MACHINE}.bin"
# A machine, multiconfig, or local.conf should override this
ASU_DEPENDS ??= ""
ASU_MCDEPENDS ??= ""
ASU_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
ASU_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
ASU_IMAGE_NAME ??= "asu-${MACHINE}"

# Default is for the multilib case (without the extension .elf)
ASU_FILE ??= "${ASU_DEPLOY_DIR}/${ASU_IMAGE_NAME}"
ASU_FILE[vardepsexclude] = "ASU_DEPLOY_DIR"

do_fetch[depends] += "${ASU_DEPENDS}"
do_fetch[mcdepends] += "${ASU_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${ASU_FILE}.elf ]; then
        echo "Unable to find ASU_FILE (${ASU_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${ASU_FILE}.elf ${D}/boot/${PN}.elf
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('ASU_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${ASU_FILE}.elf ${DEPLOYDIR}/${ASU_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"

def check_asu_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('ASU_DEPENDS') and not d.getVar('ASU_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        if not os.path.exists(d.getVar('ASU_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('ASU_FILE')
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nSee the meta-xilinx-core README." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${ASU_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${ASU_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${ASU_FILE}.elf:True')

python() {
    # Need to allow bbappends to change the check
    check_asu_vars(d)

    # Fix the mcdepends dependency format: mc:from-mc:to-mc:recipe:task
    # If the value is 'mc::' we'll adjust it to be mc:BB_CURRENT_MC: (temporary workaround)
    # If the value is 'mc:default:' we'll adjuts it to be mc:: (temporary workaround for bitbake bug)
    mcdepend = d.getVar('ASU_MCDEPENDS')
    if mcdepend:
        if d.getVar('BB_CURRENT_MC') == 'default':
            d.setVar('ASU_MCDEPENDS', mcdepend.replace('mc:default:', 'mc::'))
        else:
            d.setVar('ASU_MCDEPENDS', mcdepend.replace('mc::', 'mc:${BB_CURRENT_MC}:'))
}

