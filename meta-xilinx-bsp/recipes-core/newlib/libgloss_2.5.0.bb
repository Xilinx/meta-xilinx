
require newlib.inc

DEPENDS += "newlib"

do_configure() {
	${S}/libgloss/configure ${CONFIGUREOPTS}
}

do_install_prepend() {
	# install doesn't create this itself
	install -d ${D}${prefix}/${TARGET_SYS}/lib
}

FILES_${PN} += "${libdir}/*.ld ${libdir}/*.specs"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
