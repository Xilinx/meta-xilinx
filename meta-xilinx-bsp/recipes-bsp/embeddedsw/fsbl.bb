DESCRIPTION = "Xilinx First Stage Boot Loader"

LICENSE = "MIT"

PROVIDES = "virtual/fsbl"

INHERIT_DEFAULT_DEPENDS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"

# Specify a default in case boardvariant isn't available
BOARDVARIANT_ARCH ??= "${MACHINE_ARCH}"
PACKAGE_ARCH = "${BOARDVARIANT_ARCH}"

# Default would be a multiconfig (versal) build
# For this to work, BBMULTICONFIG += "fsbl-fw" must be in the user's local.conf!
FSBL_DEPENDS ??= ""
FSBL_MCDEPENDS ??= "mc::fsbl-fw:fsbl-firmware:do_deploy"

# This must be defined to the file output by whatever is providing the fsbl-firmware
# The following sets the default, but the BSP may select a different name
FSBL_IMAGE_NAME ??= "fsbl"
FSBL_DEPLOY_DIR ??= "${TOPDIR}/tmp-fsbl-fw/deploy/images/${MACHINE}"

# Default is for the multilib case (without the extension .elf/.bin)
FSBL_FILE ??= "${FSBL_DEPLOY_DIR}/${FSBL_IMAGE_NAME}"
FSBL_FILE[vardepsexclude] = "FSBL_DEPLOY_DIR"

do_fetch[depends] += "${FSBL_DEPENDS}"
do_fetch[mcdepends] += "${FSBL_MCDEPENDS}"

inherit update-alternatives deploy

BINARY_NAME = "${PN}"
BINARY_EXT = ".elf"
BINARY_ID = "${@d.getVar('SRCPV') if d.getVar('SRCPV') else d.getVar('PR') }"

do_install() {
    if [ ! -e ${FSBL_FILE}${BINARY_EXT} ]; then
        echo "Unable to find FSBL_FILE (${FSBL_FILE}${BINARY_EXT})"
        exit 1
    fi

    install -Dm 0644 ${FSBL_FILE}${BINARY_EXT} ${D}/boot/${BINARY_NAME}-${BINARY_ID}${BINARY_EXT}
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

ALTERNATIVE_${PN} = "fsbl"
ALTERNATIVE_TARGET[fsbl] = "/boot/${BINARY_NAME}-${BINARY_ID}${BINARY_EXT}"
ALTERNATIVE_LINK_NAME[fsbl] = "/boot/${BINARY_NAME}${BINARY_EXT}"

INSANE_SKIP_${PN} = "arch"
INSANE_SKIP_${PN}-dbg = "arch"

SYSROOT_DIRS += "/boot"
FILES_${PN} = "/boot/${BINARY_NAME}-${BINARY_ID}${BINARY_EXT}"
