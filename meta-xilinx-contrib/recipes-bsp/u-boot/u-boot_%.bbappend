FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://minized-u-boot.patch"

HAS_PLATFORM_INIT ??= " \
		zynq_minized_config \
		"

