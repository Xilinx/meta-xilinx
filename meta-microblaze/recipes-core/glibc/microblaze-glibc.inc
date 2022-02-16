# Workaround for microblaze being unable to boot
FILESEXTRAPATHS:append:microblaze := ":${THISDIR}/files"

SRC_URI:append:microblaze = " \
	file://0001-microblaze-Use-the-correct-select-syscall-BZ-28883.patch \
	file://0001-microblaze-Add-missing-implementation-when-__ASSUME_.patch \
"
