require xen-xilinx_4.18.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://xen-guest-example.cfg \
    "

RDEPENDS:${PN}-efi += "bash python3"

do_install:append() {
    install -d -m 0755 ${D}${sysconfdir}/xen
    install -m 0644 ${WORKDIR}/xen-guest-example.cfg ${D}${sysconfdir}/xen/xen-guest-example.cfg
}

do_deploy:append() {
    # Mimic older behavior for compatibility
    if [ -f ${DEPLOYDIR}/xen-${MACHINE} ]; then
        ln -s xen-${MACHINE} ${DEPLOYDIR}/xen
    fi

    if [ -f ${DEPLOYDIR}/xen-${MACHINE}.gz ]; then
        ln -s xen-${MACHINE}.gz ${DEPLOYDIR}/xen.gz
    fi

    if [ -f ${DEPLOYDIR}/xen-${MACHINE}.efi ]; then
        ln -s xen-${MACHINE}.efi ${DEPLOYDIR}/xen.efi
    fi
}

FILES:${PN} += " \
    ${sysconfdir}/xen/xen-guest-example.cfg \
    "
