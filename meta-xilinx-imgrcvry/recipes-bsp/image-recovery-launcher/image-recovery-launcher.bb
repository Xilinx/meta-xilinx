DESCRIPTION = "Launch Image recovery Web"
SUMMARY = "Image recovery web"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://image-recovery-launcher.sh \
"

REQUIRED_DISTRO_FEATURES = "sysvinit"

inherit update-rc.d features_check

INITSCRIPT_NAME = "image-recovery-launcher.sh"
INITSCRIPT_PARAMS = "start 60 5 ."

do_install () {
        install -d ${D}${sysconfdir}/init.d/
        install -m 0755 ${WORKDIR}/image-recovery-launcher.sh ${D}${sysconfdir}/init.d/
}

FILES:${PN} += "${sysconfdir}/init.d/image-recovery-launcher.sh"
