FILESEXTRAPATHS:append := ":${THISDIR}/gcc-15"

SRC_URI += " \
    file://revert-microblaze-mulitlib-hack.patch \
    file://additional-microblaze-multilibs.patch \
"
