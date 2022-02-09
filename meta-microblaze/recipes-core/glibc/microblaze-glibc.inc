# Workaround for microblaze being unable to boot
FILESEXTRAPATHS:append:microblaze := ":${THISDIR}/files"

SRC_URI:append:microblaze = " \
	file://select-use-newselect-syscall.patch \
	file://0001-microblaze-Add-missing-implementation-when-__ASSUME_.patch \
"
