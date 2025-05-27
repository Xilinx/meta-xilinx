DESCRIPTION = "Platform Loader and Manager"
SUMMARY = "Platform Loader and Manager for Versal devices"

LICENSE = "CLOSED"

PROVIDES = "virtual/plm"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the plm-firmware in the deploy
# directory, named "plm-${MACHINE}.elf"
# A machine, multiconfig, or local.conf should override this
PLM_DEPENDS ??= ""
PLM_MCDEPENDS ??= ""
PLM_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PLM_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
PLM_IMAGE_NAME ??= "plm-${MACHINE}"

# Default is for the multilib case (without the extension .elf)
PLM_FILE ??= "${PLM_DEPLOY_DIR}/${PLM_IMAGE_NAME}"
PLM_FILE[vardepsexclude] = "PLM_DEPLOY_DIR"

do_fetch[depends] += "${PLM_DEPENDS}"
do_fetch[mcdepends] += "${PLM_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${PLM_FILE}.elf ]; then
        echo "Unable to find PLM_FILE (${PLM_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${PLM_FILE}.elf ${D}/boot/${PN}.elf
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('PLM_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${PLM_FILE}.elf ${DEPLOYDIR}/${PLM_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"

def check_plm_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('PLM_DEPENDS') and not d.getVar('PLM_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        if not os.path.exists(d.getVar('PLM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PLM_FILE')
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nSee the meta-xilinx-core README." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PLM_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${PLM_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PLM_FILE}.elf:True')

python() {
    # Need to allow bbappends to change the check
    check_plm_vars(d)

    # Fix the mcdepends dependency format: mc:from-mc:to-mc:recipe:task
    # If the value is 'mc::' we'll adjust it to be mc:BB_CURRENT_MC: (temporary workaround)
    # If the value is 'mc:default:' we'll adjuts it to be mc:: (temporary workaround for bitbake bug)
    mcdepend = d.getVar('PLM_MCDEPENDS')
    if mcdepend:
        if d.getVar('BB_CURRENT_MC') == 'default':
            d.setVar('PLM_MCDEPENDS', mcdepend.replace('mc:default:', 'mc::'))
        else:
            d.setVar('PLM_MCDEPENDS', mcdepend.replace('mc::', 'mc:${BB_CURRENT_MC}:'))
}

