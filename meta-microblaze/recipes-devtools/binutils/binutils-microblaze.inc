FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

LDGOLD_ALTS:microblaze = ""
USE_ALTERNATIVES_FOR:remove:microblaze = "gprof"

# Our changes are all local, no real patch-status
ERROR_QA:remove = "patch-status"

SRC_URI += " \
    file://0057-microblaze-Refresh-all-context-for-binutils-2.46.patch \
"
