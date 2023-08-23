# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://monitor-hotplug.sh \
    file://99-monitor-hotplug.rules \
    "

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/monitor-hotplug.sh ${D}${bindir}

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-monitor-hotplug.rules ${D}${sysconfdir}/udev/rules.d/99-monitor-hotplug.rules
}

FILES:${PN} += "${sysconfdir}/udev/rules.d/*"
