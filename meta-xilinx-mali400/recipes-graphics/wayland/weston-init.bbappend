FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG:append = " no-idle-timeout"

# Disable weston system service — kmscon + agetty handles display
# login and launches weston via the user's profile.
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

# kmscon provides the display console; the profile script launches
# weston after the user authenticates via kmscon + agetty.
RDEPENDS:${PN} += "kmscon"

SRC_URI += "file://kmscon-weston.sh"

do_install:append() {
    install -d ${D}${sysconfdir}/profile.d
    install -m 0644 ${WORKDIR}/kmscon-weston.sh \
        ${D}${sysconfdir}/profile.d/kmscon-weston.sh
}

FILES:${PN} += "${sysconfdir}/profile.d/kmscon-weston.sh"
