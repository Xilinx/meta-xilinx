FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://monitor-hotplug.sh \
    file://99-monitor-hotplug.rules \
    "

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/monitor-hotplug.sh ${D}${bindir}

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/99-monitor-hotplug.rules ${D}${sysconfdir}/udev/rules.d/99-monitor-hotplug.rules
}

FILES:${PN} += "${sysconfdir}/udev/rules.d/*"
