FILESEXTRAPATHS:append:microblaze := ":${THISDIR}/files"
SRC_URI:append:microblaze = " \
    file://use-older-symver.patch \
"
