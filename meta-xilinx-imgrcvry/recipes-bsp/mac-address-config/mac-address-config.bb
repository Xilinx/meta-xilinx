SUMMARY = "MAC address configuration from EEPROM"
DESCRIPTION = "Configures network interface MAC addresses from I2C EEPROM at address 0x54"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} += "busybox util-linux-hexdump i2c-tools"
SRC_URI = "file://mac-address-config.sh"

S = "${WORKDIR}"

REQUIRED_DISTRO_FEATURES = "sysvinit"

inherit update-rc.d features_check

INITSCRIPT_NAME = "mac-address-config.sh"
INITSCRIPT_PARAMS = "start 05 S ."

do_install() {
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/mac-address-config.sh ${D}${sysconfdir}/init.d/
}
