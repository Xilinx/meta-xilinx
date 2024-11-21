QEMU_SYSTEM:aarch64 = "qemu-system-aarch64"

# Only include the sysvinit scripts if sysvinit is enabled.
do_install:append () {
    if [ -e ${D}/usr/lib/xen/bin/pygrub ]; then
        sed -i -e '1c#!/usr/bin/env python3' ${D}/usr/lib/xen/bin/pygrub
    fi

    if [ "${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'sysvinit', '', d)}" != 'sysvinit' ]; then
        rm -f ${D}/etc/init.d/xendomains
        rm -f ${D}/etc/init.d/xencommons
        rm -f ${D}/etc/init.d/xendriverdomain
        rm -f ${D}/etc/init.d/xen-watchdog
    fi
}

# If we're in a hybrid configuration, we want to stop the system from
# running any Xen sysvinit scripts
# This has a side effect of, on a hybrid system, if the init manager is
# sysvinit, the user will need to manually enable Xen.
INHIBIT_UPDATERCD_BBCLASS = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '1', '', d)}"

FILES:${PN} += " \
    ${libdir}/xen/bin/init-dom0less \
    ${libdir}/xen/bin/get_overlay \
    ${libdir}/xen/bin/get_overlay.sh \
    "
