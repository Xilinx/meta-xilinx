FILESEXTRAPATHS:append := ":${THISDIR}/gcc-16"

SRC_URI += " \
    file://revert-microblaze-mulitlib-hack.patch \
    file://additional-microblaze-multilibs.patch \
"
