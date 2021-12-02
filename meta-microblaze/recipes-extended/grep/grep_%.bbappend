FILESEXTRAPATHS:append:microblaze := ":${THISDIR}/files"
SRC_URI:append:microblaze = " \
    file://m4-stack-direction-microblaze.patch \
"
