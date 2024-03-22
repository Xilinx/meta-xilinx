#
# Copyright (C) 2024, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# This bbclass provides infrastructure to package and deploy firmware baremetal
# or freertos application elf or bin files to linux root filesystem under
# /lib/firmware directory.

inherit deploy

INHERIT_DEFAULT_DEPENDS = "1"

# Since we're just copying, we can run any config.
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default expects the user to provide the fw app in the deploy directory.
# A machine, multiconfig, or local.conf should override this.
FW_NAME ??= ""
TARGET_MC ??= ""
FW_DEPENDS ??= ""
FW_MCDEPENDS ??= ""
FW_DEPLOY_DIR ??= "${DEPLOY_DIR_IMAGE}"
FW_DEPLOY_DIR[vardepsexclude] += "TOPDIR"
FW_IMAGE_NAME ??= "${FW_NAME}-${MACHINE}-${TARGET_MC}"

# Default is for the multilib case (without the extension .elf/.bin)
FW_FILE ??= "${FW_DEPLOY_DIR}/${FW_IMAGE_NAME}"
FW_FILE[vardepsexclude] = "FW_DEPLOY_DIR"

do_fetch[depends] += "${FW_DEPENDS}"
do_fetch[mcdepends] += "${FW_MCDEPENDS}"

# Set default destination directory is /lib/firmware, user can change this value
# to /boot directory depending on requirement.
DESTDIR ??= "${nonarch_base_libdir}/firmware/xilinx"
SYSROOT_DIRS += "/boot"

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"

do_install() {
    if [ ! -e ${FW_FILE}.elf ]; then
        echo "Unable to find FW_FILE (${FW_FILE}.elf)"
        exit 1
    fi

    install -Dm 0644 ${FW_FILE}.elf ${D}${DESTDIR}/${FW_IMAGE_NAME}.elf
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('FW_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${FW_FILE}.elf ${DEPLOYDIR}/${FW_IMAGE_NAME}.elf
        install -Dm 0644 ${FW_FILE}.bin ${DEPLOYDIR}/${FW_IMAGE_NAME}.bin
    fi
}

FILES:${PN} += "${DESTDIR}/${FW_IMAGE_NAME}*"

def check_fw_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('FW_DEPENDS') and not d.getVar('FW_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        fail = False
        if not os.path.exists(d.getVar('FW_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('FW_FILE')
            fail = True
        if not os.path.exists(d.getVar('FW_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('FW_FILE')
            fail = True
        if fail:
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nSee the meta-xilinx-core README." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${FW_FILE}.elf file://${FW_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${FW_FILE}.elf:True ${FW_FILE}.bin:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${FW_FILE}.elf:True ${FW_FILE}.bin:True')

python() {
    # Need to allow bbappends to change the check
    check_fw_vars(d)
}
