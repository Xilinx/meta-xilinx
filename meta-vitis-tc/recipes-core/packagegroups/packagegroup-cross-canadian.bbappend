# Avoid installing all of the alternative toolchains
# due to multilib enabled in the primary toolchain.

# Avoid GDB, does not currently build for microblaze
GDB:xilinx-standalone:baremetal-multilib-tc:microblaze = ""

RDEPENDS:${PN}:xilinx-standalone:baremetal-multilib-tc = " \
    ${BINUTILS} \
    ${GCC} \
    ${GDB} \
    meta-environment-${MACHINE} \
"
