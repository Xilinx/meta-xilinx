SUMMARY = "Host SDK package for standalone cross canadian toolchain"
PN = "packagegroup-stadalone-cross-canadian-${MACHINE}"

inherit cross-canadian packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

# Use indirection to stop these being expanded prematurely
BINUTILS = "binutils-cross-canadian-${TRANSLATED_TARGET_ARCH}"
GCC = "gcc-cross-canadian-${TRANSLATED_TARGET_ARCH}"
GDB = "gdb-cross-canadian-${TRANSLATED_TARGET_ARCH}"

# Create the links to the multilib toolchain components
GNUTCLINKS = "standalone-gnu-toolchain-canadian-${TARGET_SYS}"

# Without the := the eval during do_package is occasionally missing multilibs
RDEPENDS:${PN}:xilinx-standalone := " \
    ${@all_multilib_tune_values(d, 'GNUTCLINKS')} \
    ${BINUTILS} \
    ${GCC} \
    ${GDB} \
    meta-environment-${MACHINE} \
"
