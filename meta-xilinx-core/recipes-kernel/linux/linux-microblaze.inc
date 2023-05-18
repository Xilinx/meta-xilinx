SRC_URI += " \
    file://microblaze_generic.cfg \
    "

KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' features/microblaze-systemd/microblaze-systemd.scc', '', d)}"

# MicroBlaze is a uImage target, but its not called 'uImage' instead it is called 'linux.bin.ub'
python () {
    if d.getVar('KERNEL_IMAGETYPE', True).endswith('.ub'):
        d.setVar('DEPENDS', "%s u-boot-mkimage-native" % d.getVar('DEPENDS', True))
}

# Disable buildpaths QA check warnings for linux-xlnx-scr
INSANE_SKIP:${PN}-src += "buildpaths"
