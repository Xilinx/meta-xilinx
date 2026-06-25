SUMMARY = "Image Recovery Boot Binary"
DESCRIPTION = "Generate BIF and BIN for Image Recovery using virtual/imgrcvry and virtual/fsbl"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy bootgen-bif

DEPENDS += "bootgen-native"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:k26-smk-kr-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k26-smk-kv-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k24-smk-kd-sdt = "${MACHINE}"

BIF_FILE_PATH = "${B}/${PN}.bif"

BIF_PARTITION_ATTR = "fsbl web-img imgrcvry-elf"

FSBL_IMAGE_NAME ??= "fsbl-${MACHINE}"
IMGRCVRY_IMAGE_NAME ??= "image-recovery-${MACHINE}"
IMGRCVRY_WEBIMG_NAME ??= "image-recovery-web-${MACHINE}"

IMGRCRY_BIF_MC_TARGET ?=""
IMGRCRY_BIF_MC_TARGET:zynqmp ?= "${MACHINE}-cortexa53-0-baremetal"
IMGRCVRY_BIF_DEPENDS ?= "virtual/fsbl:do_deploy image-recovery-web:do_deploy"
IMGRCVRY_BIF_MCDEPENDS ?= "mc::${IMGRCRY_BIF_MC_TARGET}:image-recovery:do_deploy"
IMGRCRY_DEPLOY_DIR ?= "${TMPDIR}-${IMGRCRY_BIF_MC_TARGET}/deploy/images/${MACHINE}/"

do_generate_bif[depends] += "${IMGRCVRY_BIF_DEPENDS}"
do_generate_bif[mcdepends] += "${IMGRCVRY_BIF_MCDEPENDS}"
do_generate_bif[vardeps] += "BIF_PARTITION_ATTR BIF_PARTITION_IMAGE BIF_FILE_PATH"

BIF_PARTITION_ATTR[fsbl] = "bootloader, destination_cpu=a53-0"
BIF_PARTITION_IMAGE[fsbl] = "${FSBL_DEPLOY_DIR}/${FSBL_IMAGE_NAME}.elf"

BIF_PARTITION_ATTR[web-img] = "load=0x10000000"
BIF_PARTITION_IMAGE[web-img] = "${DEPLOY_DIR_IMAGE}/${IMGRCVRY_WEBIMG_NAME}.img"

BIF_PARTITION_ATTR[imgrcvry-elf] = "destination_cpu=a53-0"
BIF_PARTITION_IMAGE[imgrcvry-elf] = "${IMGRCRY_DEPLOY_DIR}/${IMGRCVRY_IMAGE_NAME}.elf"

python do_generate_bif() {
    bootgen_bif_generate(d)
}

addtask do_generate_bif after do_configure before do_compile

do_compile () {
    bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} -w -o ${B}/image-recovery.bin
}
do_compile[depends] += "bootgen-native:do_populate_sysroot"

do_install () {
    :
}

do_deploy() {
    install -m 0644 ${B}/image-recovery.bin ${DEPLOYDIR}/image-recovery-${MACHINE}.bin
    ln -sf image-recovery-${MACHINE}.bin ${DEPLOYDIR}/image-recovery.bin
    install -m 0644 ${IMGRCRY_DEPLOY_DIR}/${IMGRCVRY_IMAGE_NAME}.elf ${DEPLOYDIR}/image-recovery-${MACHINE}.elf
    ln -sf image-recovery-${MACHINE}.elf ${DEPLOYDIR}/image-recovery.elf
}

addtask deploy before do_build after do_install
