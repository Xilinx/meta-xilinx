SUMMARY = "Image Recovery"
DESCRIPTION = "Image Recovery binary deploy"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:k26-smk-kr-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k26-smk-kv-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k24-smk-kd-sdt = "${MACHINE}"

DEPENDS += "bootgen-native virtual/fsbl"

inherit deploy bootgen-bif
include image-recovery-repository.inc

SRC_URI = "${IR_PATH};name=${MACHINE}_ir ${WEB_PATH};name=${MACHINE}_web"

S = "${WORKDIR}/git/lib/sw_apps/img_rcvry/src"

BIF_FILE_PATH = "${B}/${PN}.bif"

BIF_PARTITION_ATTR = "fsbl web-img imgrcvry-elf"

BIF_PARTITION_ATTR[fsbl] = "bootloader, destination_cpu=a53-0"
BIF_PARTITION_IMAGE[fsbl] = "${DEPLOY_DIR_IMAGE}/fsbl-${MACHINE}.elf"

BIF_PARTITION_ATTR[web-img] = "load=0x10000000"
BIF_PARTITION_IMAGE[web-img] = "${WORKDIR}/web.img"

BIF_PARTITION_ATTR[imgrcvry-elf] = "destination_cpu=a53-0"
BIF_PARTITION_IMAGE[imgrcvry-elf] = "${WORKDIR}/ImgRecovery.elf"

python do_generate_bif() {
    bootgen_bif_generate(d)
}

do_generate_bif[depends] += "virtual/fsbl:do_deploy"
do_generate_bif[vardeps] += "BIF_PARTITION_ATTR BIF_PARTITION_IMAGE BIF_FILE_PATH"

addtask do_generate_bif after do_configure before do_compile

do_install () {
    :
}

do_compile () {
    bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} -w -o ${B}/${PN}.bin
}

do_deploy() {
    install -m 0644 ${B}/${PN}.bin ${DEPLOYDIR}/${PN}-${MACHINE}.bin
    ln -sf ${PN}-${MACHINE}.bin ${DEPLOYDIR}/${PN}.bin
}

addtask do_deploy after do_install
