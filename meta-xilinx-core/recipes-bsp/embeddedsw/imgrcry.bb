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

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('IMGRCRY_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${IMGRCRY_FILE}.bin ${DEPLOYDIR}/${IMGRCRY_IMAGE_NAME}.bin
        install -Dm 0644 ${IMGRCRY_FILE}.elf ${DEPLOYDIR}/${IMGRCRY_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

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
}
