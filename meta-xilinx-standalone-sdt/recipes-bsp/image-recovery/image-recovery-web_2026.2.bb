SUMMARY = "Image Recovery Web Image"
DESCRIPTION = "Generate web.img from image-recovery web pages"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy

require image-recovery-src_2026.1.inc

SRC_URI = "${IMG_RCVRY_REPO};${IMG_RCVRY_BRANCHARG}"
SRCREV ?= "${IMG_RCVRY_SRCREV}"

S = "${WORKDIR}/git"

DEPENDS += "dosfstools-native mtools-native"

WEBIMG_DIR ?= "${B}/webimg"
WEBIMG_FILE ?= "${WEBIMG_DIR}/web.img"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_create_webimg() {
    dd if=/dev/zero of=${WEBIMG_FILE} bs=512 count=192
    mkfs.vfat -F 12 ${WEBIMG_FILE}
    mcopy -i ${WEBIMG_FILE} -s ${S}/misc/web_pages/* ::
    chmod 444 ${WEBIMG_FILE}
}
do_create_webimg[cleandirs] = "${WEBIMG_DIR}"
do_create_webimg[depends] += "dosfstools-native:do_populate_sysroot mtools-native:do_populate_sysroot"

addtask do_create_webimg after do_prepare_recipe_sysroot before do_install

do_install () {
    :
}

do_deploy() {
    install -m 0444 ${WEBIMG_FILE} ${DEPLOYDIR}/${PN}-${MACHINE}.img
    ln -sf ${PN}-${MACHINE}.img ${DEPLOYDIR}/${PN}.img
}

addtask deploy before do_build after do_install
