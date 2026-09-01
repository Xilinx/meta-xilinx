# MicroBlaze does not support LTTng UST
LTTNGUST:microblaze = ""

# Add MicroBlaze patches
FILESEXTRAPATHS:append := ":${THISDIR}/gdb"

LDFLAGS:append:class-target:microblaze = " -latomic"

SRC_URI:append:microblaze = " \
    file://mb32/0001-Depth-Total-number-of-inline-functions-refer-inline.patch \
    file://mb32/0002-Invalid-data-offsets-pointer-after-relaxation-Propos.patch \
    file://mb32/0003-Double-free-with-ld-no-keep-memory-Proposed-patches.patch \
    file://mb32/0004-When-unwinding-pc-value-adjust-return-pc-value.patch \
    file://mb32/0001-sim-microblaze-do-not-alias-SLR-SHR-onto-the-PC.patch \
    file://mb64/0001-Add-MicroBlaze-64-bit-support.patch \
    file://mb64/0002-Add-MicroBlaze-64-bit-support.patch \
     "
