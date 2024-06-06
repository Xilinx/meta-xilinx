require xen-xilinx_4.18.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RDEPENDS:${PN}-efi += "bash python3"

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
