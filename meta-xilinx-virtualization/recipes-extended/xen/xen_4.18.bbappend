require xen-xilinx_4.18.inc
require xen-misc.inc

SRC_URI += " \
    file://xen-guest-example.cfg \
    "

do_install:append() {
    install -d -m 0755 ${D}${sysconfdir}/xen
    install -m 0644 ${WORKDIR}/xen-guest-example.cfg ${D}${sysconfdir}/xen/xen-guest-example.cfg
}

FILES:${PN} += " \
    ${sysconfdir}/xen/xen-guest-example.cfg \
    "
