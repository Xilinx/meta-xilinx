SUMMARY = "Image_recovery specific libubootenv configuration files"
DESCRIPTION = "Image-recovery-specific libubootenv configuration for \
the initramfs: tells fw_printenv / fw_setenv which MTD partition and \
offsets hold the U-Boot environment used by the recovery boot path."
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://fw_env.config"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
}

FILES:${PN} = "${sysconfdir}/fw_env.config"
