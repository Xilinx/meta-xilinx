FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

LDGOLD_ALTS:microblaze = ""
USE_ALTERNATIVES_FOR:remove:microblaze = "gprof"
