FILESEXTRAPATHS:prepend:zynqmp := "${THISDIR}/files:"

SRC_URI:append:zynqmp = " file://weston.ini"

do_install:append:zynqmp() {
    install -Dm 0700 ${WORKDIR}/weston.ini ${D}/${sysconfdir}/xdg/weston/weston.ini
}
