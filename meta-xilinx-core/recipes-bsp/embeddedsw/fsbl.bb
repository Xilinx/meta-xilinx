DESCRIPTION = "Xilinx First Stage Boot Loader"

LICENSE = "CLOSED"

PROVIDES = "virtual/fsbl"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynq = "zynq"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the fsbl in the deploy
# directory, named "fsbl.elf"
# A machine, multiconfig, or local.conf should override this
FSBL_DEPENDS ??= ""
FSBL_MCDEPENDS ??= ""
FSBL_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
FSBL_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
FSBL_IMAGE_NAME ??= "fsbl-${MACHINE}"

# Default is for the multilib case (without the extension .elf)
FSBL_FILE ??= "${FSBL_DEPLOY_DIR}/${FSBL_IMAGE_NAME}"
FSBL_FILE[vardepsexclude] = "FSBL_DEPLOY_DIR"

do_fetch[depends] += "${FSBL_DEPENDS}"
do_fetch[mcdepends] += "${FSBL_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${FSBL_FILE}.elf ]; then
        echo "Unable to find FSBL_FILE (${FSBL_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${FSBL_FILE}.elf ${D}/boot/${PN}.elf
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('FSBL_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${FSBL_FILE}.elf ${DEPLOYDIR}/${FSBL_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"

def check_fsbl_variables(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('FSBL_DEPENDS') and not d.getVar('FSBL_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        if not os.path.exists(d.getVar('FSBL_FILE') + ".elf"):
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("The expected file %s.elf is not available.\nSet FSBL_FILE to the path with a precompiled FSBL binary. See the meta-xilinx-core README for more information." % d.getVar('FSBL_FILE'))
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${FSBL_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${FSBL_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${FSBL_FILE}.elf:True')

python() {
    # Need to allow bbappends to change the check
    check_fsbl_variables(d)
}
