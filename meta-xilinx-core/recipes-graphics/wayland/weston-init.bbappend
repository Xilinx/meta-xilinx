PACKAGECONFIG += "no-idle-timeout"

do_install:append:zynqmp() {
	sed -i -e "/^\[core\]/a gbm-format=rgb565" ${D}${sysconfdir}/xdg/weston/weston.ini
}
