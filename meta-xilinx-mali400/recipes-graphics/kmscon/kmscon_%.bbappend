FILESEXTRAPATHS:prepend := "${THISDIR}/kmscon:"

# Fix cross-compilation: genunifont is a native tool that needs
# native zlib, not the target zlib
DEPENDS += "zlib-native"
SRC_URI += "file://0001-meson-Use-native-zlib-for-genunifont.patch"

# Software DRM rendering, pango fonts, systemd
# seat management. No OpenGL (avoids drm3d which conflicts with
# render-only GPUs like mali400).
PACKAGECONFIG = "video_drm2d font_pango multi_seat"

# Disable kmscon.service auto-start — kmsconvt@tty1 is used instead
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

# Enable kmsconvt on tty1
SYSTEMD_SERVICE:${PN} += "kmsconvt@tty1.service"

# udev rule for display hotplug and service drop-in for display
# presence check
SRC_URI += " \
    file://99-kmscon-hotplug.rules \
    file://10-check-display.conf \
"

do_install:append() {
    # udev rule: restart kmsconvt when a display cable is plugged in
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-kmscon-hotplug.rules \
        ${D}${sysconfdir}/udev/rules.d/

    # systemd drop-in: fail kmsconvt if no display is connected
    install -d ${D}${systemd_system_unitdir}/kmsconvt@tty1.service.d
    install -m 0644 ${WORKDIR}/10-check-display.conf \
        ${D}${systemd_system_unitdir}/kmsconvt@tty1.service.d/

    # Enable kmsconvt@tty1
    install -d ${D}${sysconfdir}/systemd/system/getty.target.wants
    ln -sf ${systemd_system_unitdir}/kmsconvt@.service \
        ${D}${sysconfdir}/systemd/system/getty.target.wants/kmsconvt@tty1.service
}

FILES:${PN} += " \
    ${sysconfdir}/udev/rules.d \
    ${sysconfdir}/systemd/system/getty.target.wants \
    ${systemd_system_unitdir}/kmsconvt@tty1.service.d \
"
