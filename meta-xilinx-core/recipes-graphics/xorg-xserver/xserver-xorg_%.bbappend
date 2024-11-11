FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://monitor-hotplug.sh \
    file://99-monitor-hotplug.rules \
    file://0001-xf86Rotate.c-Add-required-NULL-check.patch \
    "

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/monitor-hotplug.sh ${D}${bindir}

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-monitor-hotplug.rules ${D}${sysconfdir}/udev/rules.d/99-monitor-hotplug.rules
}

FILES:${PN} += "${sysconfdir}/udev/rules.d/*"
