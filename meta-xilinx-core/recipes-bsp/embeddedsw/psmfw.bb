DESCRIPTION = "PSM Firmware"
SUMMARY = "PSM firmware for versal devices"

LICENSE = "CLOSED"

PROVIDES = "virtual/psm-firmware"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the psm-firmware in the deploy
# directory, named "psm-firmware-${MACHINE}.elf"
# A machine, multiconfig, or local.conf should override this
PSM_DEPENDS ??= ""
PSM_MCDEPENDS ??= ""
PSM_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PSM_FIRMWARE_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
PSM_FIRMWARE_IMAGE_NAME ??= "psm-firmware-${MACHINE}"

# Default is for the multilib case (without the extension .elf)
PSM_FILE ??= "${PSM_FIRMWARE_DEPLOY_DIR}/${PSM_FIRMWARE_IMAGE_NAME}"
PSM_FILE[vardepsexclude] = "PSM_FIRMWARE_DEPLOY_DIR"

do_fetch[depends] += "${PSM_DEPENDS}"
do_fetch[mcdepends] += "${PSM_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${PSM_FILE}.elf ]; then
        echo "Unable to find PSM_FILE (${PSM_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${PSM_FILE}.elf ${D}/boot/${PN}.elf
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('PSM_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${PSM_FILE}.elf ${DEPLOYDIR}/${PSM_FIRMWARE_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"

def check_psm_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('PSM_DEPENDS') and not d.getVar('PSM_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        if not os.path.exists(d.getVar('PSM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PSM_FILE')
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nSee the meta-xilinx-core README." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PSM_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${PSM_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PSM_FILE}.elf:True')

python() {
    # Need to allow bbappends to change the check
    check_psm_vars(d)

    # Fix the mcdepends dependency format: mc:from-mc:to-mc:recipe:task
    # If the value is 'mc::' we'll adjust it to be mc:BB_CURRENT_MC: (temporary workaround)
    # If the value is 'mc:default:' we'll adjuts it to be mc:: (temporary workaround for bitbake bug)
    mcdepend = d.getVar('PSM_MCDEPENDS')
    if mcdepend:
        if d.getVar('BB_CURRENT_MC') == 'default':
            d.setVar('PSM_MCDEPENDS', mcdepend.replace('mc:default:', 'mc::'))
        else:
            d.setVar('PSM_MCDEPENDS', mcdepend.replace('mc::', 'mc:${BB_CURRENT_MC}:'))
}

