SUMMARY = "Generates a qspi/ospi image using the previous generated boot.bin"
DESCRIPTION = "The qspi/ospi image is simply a sparse file with the boot.bin \
at the beginning.  This is suitable for booting qemu easily for devices that \
have the boot mode set to QSPI and OSPI."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

QEMU_FLASH_TYPE ??= "undefined"
QEMU_FLASH_SIZE ??= "256M"
QEMU_FLASH_STRIPE ??= "0"

# Magic value to avoid rootfs
QB_DEFAULT_FSTYPE = "none"

# Generate a qemuboot.conf file for this output
inherit ${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'qemuboot-xilinx', '', d)}
do_deploy[postfuncs] += "${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'do_write_qemuboot_conf', '', d)}"

PN = "qemu-${QEMU_FLASH_TYPE}"
DEPENDS = "virtual/boot-bin"

# Need this for the flash_strip tool
DEPENDS += "flashstrip-native"

# Don't allow building for microblaze MACHINE
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

# The bootbin may require us, but we require the bootbin's sysroot
do_compile[depends] += "virtual/boot-bin:do_populate_sysroot"
do_compile() {
    if [ -n "${QEMU_FLASH_TYPE}" -a "${QEMU_FLASH_TYPE}" != 'undefined' -a -n "${QEMU_FLASH_SIZE}" ]; then
        dd if=/dev/zero of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=${QEMU_FLASH_SIZE} count=0 conv=sparse
        dd if=${RECIPE_SYSROOT}/boot/BOOT.bin of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=0 conv=notrunc

        if [ "${QEMU_FLASH_STRIPE}" = "1" ]; then
            flash_strip ${B}/qemu-${QEMU_FLASH_TYPE}.bin ${B}/qemu-${QEMU_FLASH_TYPE}.bin_lo ${B}/qemu-${QEMU_FLASH_TYPE}.bin_hi
        fi
    fi
}

inherit deploy image-artifact-names

# Influences IMAGE_LINK_NAME
IMAGE_NAME_SUFFIX = ""

do_deploy() {
    if [ -n "${QEMU_FLASH_TYPE}" -a "${QEMU_FLASH_TYPE}" != 'undefined' -a -n "${QEMU_FLASH_SIZE}" ]; then
        install -Dm 644 ${B}/qemu-${QEMU_FLASH_TYPE}.bin ${DEPLOYDIR}/${IMAGE_NAME}.bin
        ln -sf ${IMAGE_NAME}.bin ${DEPLOYDIR}/${IMAGE_LINK_NAME}.bin
        ln -sf ${IMAGE_NAME}.bin ${DEPLOYDIR}/${PN}.bin

        if [ "${QEMU_FLASH_STRIPE}" = "1" ]; then
            install -Dm 644 ${B}/qemu-${QEMU_FLASH_TYPE}.bin_lo ${DEPLOYDIR}/${IMAGE_NAME}.bin_lo
            install -Dm 644 ${B}/qemu-${QEMU_FLASH_TYPE}.bin_hi ${DEPLOYDIR}/${IMAGE_NAME}.bin_hi
            ln -s ${IMAGE_NAME}.bin_lo ${DEPLOYDIR}/${IMAGE_LINK_NAME}.bin_lo
            ln -s ${IMAGE_NAME}.bin_hi ${DEPLOYDIR}/${IMAGE_LINK_NAME}.bin_hi
            ln -sf ${IMAGE_NAME}.bin_lo ${DEPLOYDIR}/${PN}.bin_lo
            ln -sf ${IMAGE_NAME}.bin_hi ${DEPLOYDIR}/${PN}.bin_hi
        fi
    fi
}

addtask do_deploy before do_build after do_compile

python() {
    qemu_flash_type = d.getVar("QEMU_FLASH_TYPE")
    if qemu_flash_type not in [ 'qspi', 'ospi' ]:
        raise bb.parse.SkipRecipe('Unknown flash type (%s)' % qemu_flash_type)

    qemu_flash_size = d.getVar("QEMU_FLASH_SIZE")
    if not qemu_flash_size:
        raise bb.parse.SkipRecipe('QEMU_FLASH_SIZE must be defined')
}

# We want to deploy this into the build directory and copy it later
IMGDEPLOYDIR ??= "${DEPLOYDIR}"

# Avoid circular dependencies
EXTRA_IMAGEDEPENDS = "virtual/boot-bin"

python() {
    def extraimage_getdepends(task):
        deps = ""
        for dep in (d.getVar('EXTRA_IMAGEDEPENDS') or "").split():
            if ":" in dep:
                deps += " %s " % (dep)
            else:
                deps += " %s:%s" % (dep, task)
        return deps

    deps = " " + extraimage_getdepends('do_populate_sysroot')
    d.appendVarFlag('do_deploy', 'depends', deps)
}
