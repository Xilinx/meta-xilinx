DESCRIPTION = "Recipe to provide a bitstream via virtual/bitstream"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"

BITSTREAM_PATH_DEPENDS ??= ""
DEPENDS += "${BITSTREAM_PATH_DEPENDS}"

# We never want to prefer this over another provider
DEFAULT_PREFERENCE = "-1"

PROVIDES = "virtual/bitstream"

COMPATIBLE_MACHINE = "$^"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Path where the bitstream can be found
BITSTREAM_PATH ?= ""

inherit deploy

BITSTREAM_NAME ?= "download"
BITSTREAM_NAME:microblaze ?= "system"

BITSTREAM_BASE_NAME ?= "${BITSTREAM_NAME}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

SYSROOT_DIRS += "/boot/bitstream"

do_install() {
    if [ ! -e ${BITSTREAM_PATH} ]; then
        echo "Unable to find BITSTREAM_PATH (${BITSTREAM_PATH})"
        exit 1
    fi
    install -d ${D}/boot/bitstream/
    install -Dm 0644 ${BITSTREAM_PATH} ${D}/boot/bitstream/${BITSTREAM_BASE_NAME}.bit
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('BITSTREAM_PATH')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${BITSTREAM_PATH} ${DEPLOYDIR}/${BITSTREAM_BASE_NAME}.bit
    fi
}

addtask deploy before do_build after do_install

FILES:${PN} += "/boot/bitstream/*.bit"

def check_bitstream_vars(d):
    # Assuming if BITSTREAM_PATH_DEPENDS exists, that the file will be available later.
    if not d.getVar('BITSTREAM_PATH_DEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        # If BITSTREAM_PATH is not found or defined, we error and instruct the user
        if not d.getVar('BITSTREAM_PATH'):
            raise bb.parse.SkipRecipe("Something is depending on virtual/bitstream and you have not provided a bitstream using BITSTREAM_PATH variable.\n See the meta-xilinx-core README.")

        if d.getVar('BITSTREAM_PATH') and not os.path.exists(d.getVar('BITSTREAM_PATH')):
            raise bb.parse.SkipRecipe("The expected bitstream file %s is not available.\nSee the meta-xilinx-core README." % d.getVar('BITSTREAM_PATH'))

python() {
    # Need to allow bbappends to change the check
    check_bitstream_vars(d)
}

