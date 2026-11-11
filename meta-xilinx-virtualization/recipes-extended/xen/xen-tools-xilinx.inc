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

# xen-tools.inc (meta-virtualization) unconditionally RDEPENDS on
# ${PN}-net-conf, which installs the systemd-networkd files above and
# bridges every "Type=ether" interface into xenbr0, moving the
# DHCP-assigned IP address from eth0 onto the bridge. Since
# DISTRO_FEATURES containing 'xen' pulls packagegroup-xen (and hence
# xen-tools) into essentially all amd-edf images, this silently hijacks
# eth0 even when Xen isn't actually booted (plain Linux boot on
# hardware/QEMU), losing the eth0 IP.
#
# TEMPORARY WORKAROUND: Make the Xen dom0 bridge networking
# opt-in rather than automatic until a proper upstream/distro-level fix
# is available (e.g. gating ${PN}-net-conf on a distro feature or image
# request instead of always RDEPENDS-ing on it). Boards/images that
# actually need dom0 bridged networking for DomU guests can re-enable
# it explicitly with:
#   IMAGE_INSTALL:append = " xen-tools-net-conf"
RDEPENDS:${PN}:remove = "${PN}-net-conf"

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
