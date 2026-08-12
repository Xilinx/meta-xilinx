# Override fancontrol configuration file, making this AMD FPGA specific
SRC_URI:append = " \
    file://0001-Restart-the-fancontrol-service-on-failure.patch \
    file://0001-Update-fancontrol-for-systems-that-can-monitor-Versa.patch \
    file://0001-Add-fancontrol-configuration-script.patch \
    file://fancontrol_config.sh \
    "

FILESEXTRAPATHS:append := "${THISDIR}/lmsensors:"

PACKAGE_ARCH = "${MACHINE_ARCH}"
SYSTEMD_AUTO_ENABLE:${PN}-fancontrol = "enable"

# Do not include default (dummy) fancontrol config package.
# Force the user to provide a board-specific package instead.
# By default, the fancontrol service will be inactive if no
# fancontrol config file is found.
RRECOMMENDS:${PN}-fancontrol = ""

RDEPENDS:${PN}-fancontrol += "freeipmi"

do_install:append() {
    install -D -m 0755 ${WORKDIR}/fancontrol_config.sh ${D}${sbindir}/fancontrol_config.sh
}

FILES:${PN}-fancontrol += "${sbindir}/fancontrol_config.sh"
