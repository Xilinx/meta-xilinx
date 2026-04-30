FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://99-dri-device.rules \
    file://weston.service.d/10-edf-overrides.conf \
"

PACKAGECONFIG:append = " no-idle-timeout"

do_install:append() {
    # Install udev rule to tag DRI card devices for systemd so
    # weston is only started when a display device is present.
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-dri-device.rules \
        ${D}${sysconfdir}/udev/rules.d/

    # Install systemd drop-in to override ExecStart with debug
    # logging and --continue-without-input, and clear WantedBy
    # so weston is only started via the udev SYSTEMD_WANTS rule
    # when a display device is present.
    install -d ${D}${systemd_system_unitdir}/weston.service.d
    install -m 0644 ${WORKDIR}/weston.service.d/10-edf-overrides.conf \
        ${D}${systemd_system_unitdir}/weston.service.d/
}

FILES:${PN} += " \
    ${sysconfdir}/udev/rules.d \
    ${systemd_system_unitdir}/weston.service.d \
"
