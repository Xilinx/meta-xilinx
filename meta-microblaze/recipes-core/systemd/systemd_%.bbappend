FILESEXTRAPATHS:append:microblaze := ":${THISDIR}/files"
SRC_URI:append:microblaze = " \
    file://0001-architecture-Add-Microblaze-architecture-to-systemd-.patch \
    file://microblaze-syscalls.patch \
    file://microblaze-disable-stack-protector.patch \
    file://microblaze-once-macro.patch \
"
