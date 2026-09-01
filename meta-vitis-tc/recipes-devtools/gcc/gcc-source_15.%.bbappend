FILESEXTRAPATHS:append := ":${THISDIR}/gcc-15"

#        file://additional-microblaze-multilibs.patch \
#

SRC_URI += " \
    file://revert-microblaze-mulitlib-hack.patch \
"
