DESCRIPTION = "PMU Firmware"

LICENSE = "MIT"

PROVIDES = "virtual/pmu-firmware"

INHERIT_DEFAULT_DEPENDS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"

# Specify a default in case boardvariant isn't available
BOARDVARIANT_ARCH ??= "${MACHINE_ARCH}"
PACKAGE_ARCH = "${BOARDVARIANT_ARCH}"

# Default would be a multiconfig (zynqmp-pmufw) build
# For this to work, BBMULTICONFIG += "zynqmp-pmufw" must be in the user's local.conf!
PMU_DEPENDS ??= ""
PMU_MCDEPENDS ??= "mc::zynqmp-pmufw:pmu-firmware:do_deploy"

# This must be defined to the file output by whatever is providing the pmu-firmware
# The following sets the default, but the BSP may select a different name
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-zynqmp-pmu"
PMU_FIRMWARE_DEPLOY_DIR ??= "${TOPDIR}/tmp-microblaze-zynqmp-pmufw/deploy/images/${MACHINE}"

# Default is for the multilib case (without the extension .elf/.bin)
PMU_FILE ??= "${PMU_FIRMWARE_DEPLOY_DIR}/${PMU_FIRMWARE_IMAGE_NAME}"
PMU_FILE[vardepsexclude] = "PMU_FIRMWARE_DEPLOY_DIR"

do_fetch[depends] += "${PMU_DEPENDS}"
do_fetch[mcdepends] += "${PMU_MCDEPENDS}"

inherit deploy update-alternatives

BINARY_NAME = "${PN}"
BINARY_EXT = ".elf"
BINARY_ID = "${@d.getVar('SRCPV') if d.getVar('SRCPV') else d.getVar('PR') }"

do_install() {
    if [ ! -e ${PMU_FILE}${BINARY_EXT} ]; then
        echo "Unable to find PMU_FILE (${PMU_FILE}${BINARY_EXT})"
        exit 1
    fi

    install -Dm 0644 ${PMU_FILE}${BINARY_EXT} ${D}/boot/${BINARY_NAME}-${BINARY_ID}${BINARY_EXT}
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('PMU_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${PMU_FILE}.elf ${DEPLOYDIR}/${PMU_FIRMWARE_IMAGE_NAME}.elf
        install -Dm 0644 ${PMU_FILE}.bin ${DEPLOYDIR}/${PMU_FIRMWARE_IMAGE_NAME}.bin
    fi
}

addtask deploy before do_build after do_install

ALTERNATIVE_${PN} = "pmufw"
ALTERNATIVE_TARGET[pmufw] = "/boot/${BINARY_NAME}-${BINARY_ID}${BINARY_EXT}"
ALTERNATIVE_LINK_NAME[pmufw] = "/boot/${BINARY_NAME}${BINARY_EXT}"

INSANE_SKIP_${PN} = "arch"
INSANE_SKIP_${PN}-dbg = "arch"

SYSROOT_DIRS += "/boot"
FILES_${PN} = "/boot/${BINARY_NAME}-${BINARY_ID}${BINARY_EXT}"
