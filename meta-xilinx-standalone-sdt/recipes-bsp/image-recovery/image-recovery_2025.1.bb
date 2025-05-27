SUMMARY = "Image Recovery"
DESCRIPTION = "Image Recovery binary deploy"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:k26-smk-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k24-smk-sdt = "${MACHINE}"

DEPENDS += "bootgen-native virtual/fsbl"

do_compile[depends] += "virtual/fsbl:do_deploy"

inherit deploy
include image-recovery-repository.inc

SRC_URI = "${IR_PATH};name=${MACHINE}_ir ${WEB_PATH};name=${MACHINE}_web"

S = "${WORKDIR}/git/lib/sw_apps/img_rcvry/src"

do_configure () {

cat > ${WORKDIR}/${PN}.bif << EOF
    the_ROM_image:
    {
        [bootloader, destination_cpu=a53-0] ${DEPLOY_DIR_IMAGE}/fsbl-${MACHINE}.elf
        [load=0x10000000] ${WORKDIR}/web.img
        [destination_cpu=a53-0] ${WORKDIR}/ImgRecovery.elf
    }
EOF
}

do_install () {
    :
}

do_compile () {
    bootgen -image ${WORKDIR}/${PN}.bif -arch ${SOC_FAMILY} -w -o ${WORKDIR}/${PN}.bin

    printf "* ${PN} static file: ${IR_PATH}\n" > ${S}/${PN}.manifest
    printf "SHA256SUM: $(sha256sum ${WORKDIR}/ImgRecovery.elf | cut -d " " -f 1)\n\n"  >> ${S}/${PN}.manifest
}

do_deploy() {
    install -m 0644 ${WORKDIR}/${PN}.bin ${DEPLOYDIR}/${PN}-${MACHINE}.bin
    ln -sf ${PN}-${MACHINE}.bin ${DEPLOYDIR}/${PN}.bin

    install -Dm 0644 ${S}/${PN}.manifest ${DEPLOYDIR}/${PN}-${MACHINE}.manifest
}

addtask do_deploy after do_install
