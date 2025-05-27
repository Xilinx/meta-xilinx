DESCRIPTION = "Image Recovery"

LICENSE = "CLOSED"

PROVIDES = "virtual/imgrcry"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

# Default expects the user to provide the imagerecovery in the deploy
# directory, named "image-recovery-${MACHINE}.bin" and "image-recovery-${MACHINE}.bin"
# A machine, multiconfig, or local.conf should override this
IMGRCRY_DEPENDS ??= ""
IMGRCRY_MCDEPENDS ??= ""
IMGRCRY_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
IMGRCRY_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
IMGRCRY_IMAGE_NAME ??= "image-recovery-${MACHINE}"

# Default is for the multilib case (without the extension .bin)
IMGRCRY_FILE ??= "${IMGRCRY_DEPLOY_DIR}/${IMGRCRY_IMAGE_NAME}"
IMGRCRY_FILE[vardepsexclude] = "IMGRCRY_DEPLOY_DIR"

do_fetch[depends] += "${IMGRCRY_DEPENDS}"
do_fetch[mcdepends] += "${IMGRCRY_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${IMGRCRY_FILE}.bin ]; then
        echo "Unable to find IMGRCRY_FILE (${IMGRCRY_FILE}.bin)"
        exit 1
    fi

    install -Dm 0644 ${IMGRCRY_FILE}.bin ${D}/boot/${PN}.bin
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('IMGRCRY_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${IMGRCRY_FILE}.bin ${DEPLOYDIR}/${IMGRCRY_IMAGE_NAME}.bin
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.bin"

def check_imgrcry_variables(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('IMGRCRY_DEPENDS') and not d.getVar('IMGRCRY_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        if not os.path.exists(d.getVar('IMGRCRY_FILE') + ".bin"):
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("The expected file %s.bin is not available.\nSet IMGRCRY_FILE to the path with a precompiled IMGRCRY binary." % d.getVar('IMGRCRY_FILE'))
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${IMGRCRY_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${IMGRCRY_FILE}.bin:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${IMGRCRY_FILE}.bin:True')

python() {
    # Need to allow bbappends to change the check
    check_imgrcry_variables(d)

    # Fix the mcdepends dependency format: mc:from-mc:to-mc:recipe:task
    # If the value is 'mc::' we'll adjust it to be mc:BB_CURRENT_MC: (temporary workaround)
    # If the value is 'mc:default:' we'll adjuts it to be mc:: (temporary workaround for bitbake bug)
    mcdepend = d.getVar('IMGRCRY_MCDEPENDS')
    if mcdepend:
        if d.getVar('BB_CURRENT_MC') == 'default':
            d.setVar('IMGRCRY_MCDEPENDS', mcdepend.replace('mc:default:', 'mc::'))
        else:
            d.setVar('IMGRCRY_MCDEPENDS', mcdepend.replace('mc::', 'mc:${BB_CURRENT_MC}:'))
}
