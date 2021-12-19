DESCRIPTION = "PSM Firmware"
SUMMARY = "PSM firmware for versal devices"

LICENSE = "MIT"

PROVIDES = "virtual/psm-firmware"

INHERIT_DEFAULT_DEPENDS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "versal"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Default would be a multiconfig (versal) build
# For this to work, BBMULTICONFIG += "versal-fw" must be in the user's local.conf!
PSM_DEPENDS ??= ""
PSM_MCDEPENDS ??= "mc::versal-fw:psm-firmware:do_deploy"

# This must be defined to the file output by whatever is providing the psm-firmware
# The following sets the default, but the BSP may select a different name
PSM_FIRMWARE_IMAGE_NAME ??= "psm-firmware-versal-mb"
PSM_FIRMWARE_DEPLOY_DIR ??= "${TOPDIR}/tmp-microblaze-versal-fw/deploy/images/${MACHINE}"

# Default is for the multilib case (without the extension .elf/.bin)
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
        install -Dm 0644 ${PSM_FILE}.bin ${DEPLOYDIR}/${PSM_FIRMWARE_IMAGE_NAME}.bin
    fi
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

SYSROOT_DIRS += "/boot"
FILES:${PN} = "/boot/${PN}.elf"
