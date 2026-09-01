FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

LDGOLD_ALTS:microblaze = ""
USE_ALTERNATIVES_FOR:remove:microblaze = "gprof"

SRC_URI += " \
    file://mb32/0001-Add-mlittle-endian-and-mbig-endian-flags.patch \
    file://mb32/0002-fixed-bug-in-GCC-so-that-It-will-support-long-0U-and.patch \
    file://mb32/0003-Compiler-will-give-error-messages-in-more-detail-for.patch \
    file://mb32/0004-Revert-ld-Remove-unused-expression-state-defsym-symb.patch \
    file://mb32/0005-Invalid-data-offsets-pointer-after-relaxation-Propos.patch \
    file://mb32/0006-Double-free-with-ld-no-keep-memory-Proposed-patches.patch \
    file://mb64/0001-Add-MicroBlaze-64-bit-support.patch \
    file://mb64/0002-Add-MicroBlaze-64-bit-support.patch \
"
