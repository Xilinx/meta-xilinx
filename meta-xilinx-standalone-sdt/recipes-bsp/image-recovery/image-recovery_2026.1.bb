SUMMARY = "Image Recovery"
DESCRIPTION = "Image Recovery ELF build and deploy"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit esw python3native deploy

require image-recovery-src_2026.1.inc

SRC_URI:append = " ${IMG_RCVRY_REPO};${IMG_RCVRY_BRANCHARG};destsuffix=image-recovery;name=image-recovery"
SRCREV_image-recovery = "${IMG_RCVRY_SRCREV}"

IMG_RCVRY_DEPENDS ??= ""
IMG_RCVRY_DEPENDS:zynqmp ??= "libxil xiltimer lwip xilffs"
DEPENDS += "${IMG_RCVRY_DEPENDS}"

ESW_COMPONENT_SRC = "/src/"
ESW_EXECUTABLE_NAME = "img_rcvry"

RCONFLICTS:${PN} = "image-recovery-linux"

do_generate_app_data() {
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0644 *.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}/
}
do_generate_app_data[dirs] = "${S}"
addtask do_generate_app_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}/
    install -m 0644 ${S}/cmake/UserConfig.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    )
}

OECMAKE_SOURCEPATH = "${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}"

do_install () {
    :
}

do_deploy() {
    install -m 0644 ${B}/${ESW_EXECUTABLE_NAME}.elf ${DEPLOYDIR}/${PN}-${MACHINE}.elf
    ln -sf ${PN}-${MACHINE}.elf ${DEPLOYDIR}/${PN}.elf
}

addtask deploy before do_build after do_install
