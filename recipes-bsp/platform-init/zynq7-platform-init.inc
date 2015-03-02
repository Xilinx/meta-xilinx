inherit zynq7-platform-paths

PROVIDES += "virtual/zynq7-platform-init"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile() {
	:
}

do_install() {
	install -d ${D}${PLATFORM_INIT_DIR}
}

python sysroot_stage_all () {
    oe.path.copytree(d.expand("${D}${PLATFORM_INIT_DIR}"), d.expand("${SYSROOT_DESTDIR}${PLATFORM_INIT_DIR}"))
}

FILES_${PN} += " \
		${PLATFORM_INIT_DIR}/ps7_init_gpl.c \
		${PLATFORM_INIT_DIR}/ps7_init_gpl.h \
		"

