# Avoid installing all of the alternative toolchains
# due to multilib enabled in the primary toolchain.

RDEPENDS:${PN}:xilinx-standalone:baremetal-multilib-tc = " \
    ${BINUTILS} \
    ${GCC} \
    ${GDB} \
    meta-environment-${MACHINE} \
"
