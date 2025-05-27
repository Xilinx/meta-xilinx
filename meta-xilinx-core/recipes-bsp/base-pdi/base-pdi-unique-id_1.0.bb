DESCRIPTION = "Base pdi unique id file - text format"
SUMMARY = "The BIF file for bootbin requires a Base pdi unique id file in a text format"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

BOOTGEN_ARCH_DEFAULT:versal = "versal"
BOOTGEN_ARCH_DEFAULT:versal-2ve-2vm = "versal_2ve_2vm"
BOOTGEN_ARCH ?= "${BOOTGEN_ARCH_DEFAULT}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BASEPDI_ID_FILE = "${B}/base-pdi-unique-id-string.txt"

inherit deploy image-artifact-names

IMAGE_NAME_SUFFIX = ""

DEPENDS += "base-pdi bootgen-native"

BASE_PDI_FILE ?= "${RECIPE_SYSROOT}/boot/base-design.pdi"

do_configure() {
    unique_id=$(bootgen -arch ${BOOTGEN_ARCH} -read ${BASE_PDI_FILE} | grep 0x18700000 | grep unique | cut -d ":" -f3 | tr -d ' ')
    if [ ! -z ${unique_id} ]; then
        bbnote "Found unique_id: ${unique_id} from basepdi"
        echo ${unique_id#0x} > ${BASEPDI_ID_FILE}
    else
        bberror "No unique_id found from base pdi"
    fi
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${BASEPDI_ID_FILE} ${DEPLOYDIR}/${IMAGE_NAME}.txt
    ln -s ${IMAGE_NAME}.txt ${DEPLOYDIR}/${IMAGE_LINK_NAME}.txt
}

addtask deploy after do_compile
