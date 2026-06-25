SUMMARY = "Generates boot.bin using bootgen tool"
DESCRIPTION = "Manages task dependencies and creation of boot.bin. Use the \
BIF_PARTITION_xyz global variables and flags to determine what makes it into \
the image."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

include machine-xilinx-${SOC_FAMILY}.inc

# Include RPU1 BIF partition attributes per SOC family.
# To enable RPU1 hello-world in the boot image, set:
#   BIF_APPS_ATTR:append = " rpu1-hello-world"
# in your machine conf or local.conf.
include rpu1-xilinx.inc

BOOTBIN_INCLUDE ?= ""

inherit deploy bootgen-bif shared-manifest-aggregate


MANIFEST_AGGREGATE_COMPONENTS:zynq = "device-tree u-boot fsbl"
MANIFEST_AGGREGATE_COMPONENTS:zynqmp = "device-tree u-boot fsbl trusted-firmware-a pmu-firmware"
MANIFEST_AGGREGATE_COMPONENTS:versal = "device-tree u-boot trusted-firmware-a plm psm-firmware"
MANIFEST_AGGREGATE_COMPONENTS:versal-net = "device-tree u-boot trusted-firmware-a plm psm-firmware"
MANIFEST_AGGREGATE_COMPONENTS:versal-2ve-2vm = "device-tree u-boot trusted-firmware-a plm"

MANIFEST_AGGREGATE_DEPLOY_NAME = "${BOOTBIN_BASE_NAME}.manifest.json"
MANIFEST_AGGREGATE_LINK_NAME = "boot.bin.manifest.json"

MANIFEST_AGGREGATE_DEPENDS:zynq = "device-tree virtual/bootloader fsbl"
MANIFEST_AGGREGATE_DEPENDS:zynqmp = "device-tree virtual/bootloader fsbl pmufw virtual/arm-trusted-firmware"
MANIFEST_AGGREGATE_DEPENDS:versal = "device-tree virtual/bootloader plmfw psmfw virtual/arm-trusted-firmware virtual/base-pdi"
MANIFEST_AGGREGATE_DEPENDS:versal-net = "device-tree virtual/bootloader plmfw psmfw virtual/arm-trusted-firmware virtual/base-pdi"
MANIFEST_AGGREGATE_DEPENDS:versal-2ve-2vm = "device-tree virtual/bootloader plmfw virtual/arm-trusted-firmware virtual/base-pdi"

# Don't allow building for microblaze MACHINE
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

PROVIDES = "virtual/boot-bin"

DEPENDS += "bootgen-native"

# Need to convert from BIF_PARTITION_ATTR to DEPENDS format
def bif_partition_attr_to_depends(d):
    depends = (d.getVar('BIF_PARTITION_ATTR') or "").split()

    # Convert bitstream to virtual/bitstream
    try:
        depends[depends.index("bitstream")] = "virtual/bitstream"
    except ValueError:
        pass

    # Convert arm-trusted-firmware to virtual/arm-trusted-firmware
    try:
        depends[depends.index("arm-trusted-firmware")] = "virtual/arm-trusted-firmware"
    except ValueError:
        pass

    # Convert u-boot-xlnx to virtual/bootloader
    try:
        depends[depends.index("u-boot-xlnx")] = "virtual/bootloader"
    except ValueError:
        pass

    return ' '.join(depends)

DEPENDS += "${@bif_partition_attr_to_depends(d)}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BIF_FILE_PATH ?= "${B}/bootgen.bif"

SRC_URI += "${@('file://' + d.getVar("BIF_FILE_PATH")) if d.getVar("BIF_FILE_PATH") != (d.getVar('B') + '/bootgen.bif') else ''}"

BOOTGEN_EXTRA_ARGS ?= ""

BIF_BITSTREAM_ATTR ?= "${@bb.utils.contains('MACHINE_FEATURES', 'fpga-overlay', '', 'bitstream', d)}"

do_patch[noexec] = "1"

python do_configure() {
    fp = d.getVar("BIF_FILE_PATH")
    
    # If using a custom BIF file, nothing to generate
    if fp == (d.getVar('B') + '/bootgen.bif'):
        bootgen_bif_generate(d)
    else:
        bb.note("Using custom BIF file: %s" % fp)
}

do_configure[vardeps] += "BIF_PARTITION_ATTR BIF_PARTITION_IMAGE BIF_COMMON_ATTR"
do_configure[vardeps] += "BIF_FSBL_ATTR BIF_BITSTREAM_ATTR BIF_ATF_ATTR BIF_DEVICETREE_ATTR BIF_SSBL_ATTR BIF_OPTIONAL_DATA"
do_configure[vardeps] += "BIF_PARTITION_ID BIF_PARTITION_NAME"

do_compile() {
    rm -f ${B}/BOOT.bin
    if [ "${BIF_FILE_PATH}" != "${B}/bootgen.bif" ];then
        BIF_FILE_PATH="${WORKDIR}${BIF_FILE_PATH}"
    fi
    bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} ${BOOTGEN_EXTRA_ARGS} -w -o ${B}/BOOT.bin
    if [ ! -e ${B}/BOOT.bin ]; then
        bbfatal "bootgen failed. See log"
    fi
}

do_install() {
    install -d ${D}/boot
    install -m 0644 ${B}/BOOT.bin ${D}/boot/BOOT.bin
}

inherit image-artifact-names

BOOTBIN_LINK_NAME ?= "BOOT-${MACHINE}"
BOOTBIN_BASE_NAME ?= "BOOT-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${B}/BOOT.bin ${DEPLOYDIR}/${BOOTBIN_BASE_NAME}.bin
    ln -sf ${BOOTBIN_BASE_NAME}.bin ${DEPLOYDIR}/${BOOTBIN_LINK_NAME}.bin
    ln -sf ${BOOTBIN_BASE_NAME}.bin ${DEPLOYDIR}/boot.bin

    install -d ${DEPLOYDIR}/boot.bin-extracted
    install -m 0644 ${B}/* ${DEPLOYDIR}/boot.bin-extracted/.
    rm -f ${DEPLOYDIR}/boot.bin-extracted/BOOT.bin
}

FILES:${PN} += "/boot/BOOT.bin"
SYSROOT_DIRS += "/boot"

addtask deploy before do_build after do_compile

# We want to deploy this into the build directory and copy it later
IMGDEPLOYDIR ??= "${DEPLOYDIR}"
IMAGE_LINK_NAME = "${BOOTBIN_LINK_NAME}"
IMAGE_NAME = "${BOOTBIN_BASE_NAME}"

inherit ${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'qemuboot-xilinx', '', d)}
do_deploy[postfuncs] += "${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'do_write_qemuboot_conf', '', d)}"

QEMU_FLASH_TYPE ??= "undefined"

# Avoid circular dependencies
EXTRA_IMAGEDEPENDS:remove := "${PN}"
EXTRA_IMAGEDEPENDS:remove = "virtual/cdo extract-cdo"
EXTRA_IMAGEDEPENDS:remove = "virtual/boot-bin"
EXTRA_IMAGEDEPENDS:remove = "qemu-image-empty:do_image_complete"
EXTRA_IMAGEDEPENDS:remove := "qemu-${QEMU_FLASH_TYPE} qemu-${QEMU_FLASH_TYPE}:do_deploy"
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
