DESCRIPTION = "PMU Firmware"

LICENSE = "CLOSED"

PROVIDES = "virtual/pmu-firmware"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the pmu-firmware in the deploy
# directory, named "pmu-firmware-${MACHINE}.elf" and "pmu-firmware-${MACHINE}.bin"
# A machine, multiconfig, or local.conf should override this
PMU_DEPENDS ??= ""
PMU_MCDEPENDS ??= ""
PMU_FIRMWARE_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
PMU_FIRMWARE_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-${MACHINE}"

# Default is for the multilib case (without the extension .elf)
PMU_FILE ??= "${PMU_FIRMWARE_DEPLOY_DIR}/${PMU_FIRMWARE_IMAGE_NAME}"
PMU_FILE[vardepsexclude] = "PMU_FIRMWARE_DEPLOY_DIR"

do_fetch[depends] += "${PMU_DEPENDS}"
do_fetch[mcdepends] += "${PMU_MCDEPENDS}"

inherit deploy

do_install() {
    if [ ! -e ${PMU_FILE}.elf ]; then
        echo "Unable to find PMU_FILE (${PMU_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${PMU_FILE}.elf ${D}/boot/${PN}.elf
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('PMU_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${PMU_FILE}.elf ${DEPLOYDIR}/${PMU_FIRMWARE_IMAGE_NAME}.elf
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"

def check_pmu_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('PMU_DEPENDS') and not d.getVar('PMU_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        if not os.path.exists(d.getVar('PMU_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PMU_FILE')
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s  See the meta-xilinx-core README." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PMU_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${PMU_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PMU_FILE}.elf:True')


python() {
    # Need to allow bbappends to change the check
    check_pmu_vars(d)

    # Fix the mcdepends dependency format: mc:from-mc:to-mc:recipe:task
    # If the value is 'mc::' we'll adjust it to be mc:BB_CURRENT_MC: (temporary workaround)
    # If the value is 'mc:default:' we'll adjuts it to be mc:: (temporary workaround for bitbake bug)
    mcdepend = d.getVar('PMU_MCDEPENDS')
    if mcdepend:
        if d.getVar('BB_CURRENT_MC') == 'default':
            d.setVar('PMU_MCDEPENDS', mcdepend.replace('mc:default:', 'mc::'))
        else:
            d.setVar('PMU_MCDEPENDS', mcdepend.replace('mc::', 'mc:${BB_CURRENT_MC}:'))
}
