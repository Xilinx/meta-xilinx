FILESEXTRAPATHS:prepend := "${THISDIR}/kmscon:"

# Software DRM rendering, pango fonts, systemd
# seat management. No OpenGL (avoids drm3d which conflicts with
# render-only GPUs like mali400).
PACKAGECONFIG = "video_drm2d font_pango multi_seat"

# Disable kmscon.service auto-start — kmsconvt@tty1 is used instead
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

# Enable kmsconvt on tty1
SYSTEMD_SERVICE:${PN} += "kmsconvt@tty1.service"

# udev rule for display hotplug, service drop-in for display presence
# check and a preset that keeps agetty off the VTs kmscon owns
SRC_URI += " \
    file://99-kmscon-hotplug.rules \
    file://10-check-display.conf \
    file://80-kmscon-no-vt-getty.preset \
"

do_install:append() {
    # udev rule: restart kmsconvt when a display cable is plugged in
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/99-kmscon-hotplug.rules \
        ${D}${sysconfdir}/udev/rules.d/

    # systemd drop-in: skip kmsconvt if no display is connected
    install -d ${D}${systemd_system_unitdir}/kmsconvt@tty1.service.d
    install -m 0644 ${UNPACKDIR}/10-check-display.conf \
        ${D}${systemd_system_unitdir}/kmsconvt@tty1.service.d/

    # Enable kmsconvt@tty1
    install -d ${D}${sysconfdir}/systemd/system/getty.target.wants
    ln -sf ${systemd_system_unitdir}/kmsconvt@.service \
        ${D}${sysconfdir}/systemd/system/getty.target.wants/kmsconvt@tty1.service

    # kmscon owns tty1, so keep systemd's preset from enabling agetty there
    install -d ${D}${systemd_unitdir}/system-preset
    install -m 0644 ${UNPACKDIR}/80-kmscon-no-vt-getty.preset \
        ${D}${systemd_unitdir}/system-preset/
}

FILES:${PN} += " \
    ${sysconfdir}/udev/rules.d \
    ${sysconfdir}/systemd/system/getty.target.wants \
    ${systemd_unitdir}/system-preset/80-kmscon-no-vt-getty.preset \
    ${systemd_system_unitdir}/kmsconvt@tty1.service.d \
"
