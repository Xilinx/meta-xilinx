QEMU_SYSTEM:aarch64 = "qemu-system-aarch64"

# xen-xilinx_4.21.inc (required by the xen-tools bbappend before this file)
# fully overrides SRC_URI to switch to the Xilinx xen.git fork, which wipes
# out the systemd-networkd config files xen-tools.inc adds via SRC_URI +=
# (10-ether.network, 10-xenbr0.netdev, 10-xenbr0.network). Add them back
# here; they are still found via meta-virtualization's FILESPATH.
SRC_URI += "file://10-ether.network \
            file://10-xenbr0.netdev \
            file://10-xenbr0.network \
            "

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
