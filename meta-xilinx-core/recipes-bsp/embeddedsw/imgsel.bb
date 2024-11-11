DESCRIPTION = "Image Selector"

LICENSE = "CLOSED"

PROVIDES = "virtual/imgsel"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

# Default expects the user to provide the imageselector in the deploy
# directory, named "image-selector-${MACHINE}.bin" and "image-selector-${MACHINE}.bin"
# A machine, multiconfig, or local.conf should override this
IMGSEL_DEPENDS ??= ""
IMGSEL_MCDEPENDS ??= ""
IMGSEL_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
IMGSEL_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
IMGSEL_IMAGE_NAME ??= "image-selector-${MACHINE}"

# Default is for the multilib case (without the extension .bin)
IMGSEL_FILE ??= "${IMGSEL_DEPLOY_DIR}/${IMGSEL_IMAGE_NAME}"
IMGSEL_FILE[vardepsexclude] = "IMGSEL_DEPLOY_DIR"

do_fetch[depends] += "${IMGSEL_DEPENDS}"
do_fetch[mcdepends] += "${IMGSEL_MCDEPENDS}"

inherit deploy

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('IMGSEL_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${IMGSEL_FILE}.bin ${DEPLOYDIR}/${IMGSEL_IMAGE_NAME}.bin
        install -Dm 0644 ${IMGSEL_FILE}.elf ${DEPLOYDIR}/${IMGSEL_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

#SYSROOT_DIRS += "/boot"
#FILES:${PN} = "/boot/${PN}.bin"

def check_imgsel_variables(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('IMGSEL_DEPENDS') and not d.getVar('IMGSEL_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        if not os.path.exists(d.getVar('IMGSEL_FILE') + ".bin"):
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("The expected file %s.bin is not available.\nSet IMGSEL_FILE to the path with a precompiled IMGSEL binary." % d.getVar('IMGSEL_FILE'))
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${IMGSEL_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${IMGSEL_FILE}.bin:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${IMGSEL_FILE}.bin:True')

python() {
    # Need to allow bbappends to change the check
    check_imgsel_variables(d)
}
