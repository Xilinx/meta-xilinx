SUMMARY = "Simple test application"
SECTION = "PETALINUX/apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INSANE_SKIP:${PN} = "arch"



do_install() {
 # Install firmware into /lib/firmware on target
 install -d ${D}/lib/firmware
 install -m 0644 ${FW_PATH} ${D}/lib/firmware/${FW_NAME}
}
FILES:${PN} = "/lib/firmware/${FW_NAME}"

FW_NAME ?= ""
FW_PATH ?= ""
