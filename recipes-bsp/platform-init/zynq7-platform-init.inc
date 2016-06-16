inherit zynq7-platform-paths

PROVIDES += "virtual/zynq7-platform-init"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SYSROOT_DIRS += "${PLATFORM_INIT_DIR}"

do_compile() {
	:
}

do_install() {
	install -d ${D}${PLATFORM_INIT_DIR}
}

FILES_${PN} += " \
		${PLATFORM_INIT_DIR}/ps7_init_gpl.c \
		${PLATFORM_INIT_DIR}/ps7_init_gpl.h \
		"

