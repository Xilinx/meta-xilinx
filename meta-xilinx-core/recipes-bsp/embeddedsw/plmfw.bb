DESCRIPTION = "Platform Loader and Manager"
SUMMARY = "Platform Loader and Manager for Versal devices"

LICENSE = "CLOSED"

PROVIDES = "virtual/plm"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the plm-firmware in the deploy
# directory, named "plm-${MACHINE}.elf" and "plm-${MACHINE}.bin"
# A machine, multiconfig, or local.conf should override this
PLM_DEPENDS ??= ""
PLM_MCDEPENDS ??= ""
PLM_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PLM_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
PLM_IMAGE_NAME ??= "plm-${MACHINE}"

# Default is for the multilib case (without the extension .elf/.bin)
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
        install -Dm 0644 ${PLM_FILE}.bin ${DEPLOYDIR}/${PLM_IMAGE_NAME}.bin
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
        fail = False
        if not os.path.exists(d.getVar('PLM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PLM_FILE')
            fail = True
        if not os.path.exists(d.getVar('PLM_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PLM_FILE')
            fail = True
        if fail:
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nSee the meta-xilinx-core README." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PLM_FILE}.elf file://${PLM_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${PLM_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PLM_FILE}.elf:True ${PLM_FILE}.bin:True')

python() {
    # Need to allow bbappends to change the check
    check_plm_vars(d)
}

