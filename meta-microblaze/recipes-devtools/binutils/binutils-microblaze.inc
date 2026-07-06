FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

LDGOLD_ALTS:microblaze = ""
USE_ALTERNATIVES_FOR:remove:microblaze = "gprof"

SRC_URI += " \
    file://0057-microblaze-Refresh-all-context-for-binutils-2.46.patch \
"
