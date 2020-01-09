include gcc-xilinx-standalone.inc

# This is still missing a way to remove infiniarray, if we really have to we could use _remove
EXTRA_OECONF_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone = " \
    ${GCC_CONFIGURE_COMMON_XILINX_STANDALONE} \
    --program-prefix=${TARGET_PREFIX} \
    --without-local-prefix \
    ${EXTRA_OECONF_PATHS} \
"


# Cortex-A53
EXTRA_OECONF_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_cortexa53 = " \
        ${GCC_CONFIGURE_A53} \
"

# Cortex-A72
EXTRA_OECONF_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_cortexa72 = " \
        ${GCC_CONFIGURE_A72} \
"

# Cortex-R5
EXTRA_OECONF_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_cortexr5 = " \
        ${GCC_CONFIGURE_R5} \
"

# MB
EXTRA_OECONF_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_zynqmp-pmu = " \
	${GCC_CONFIGURE_MB} \
"

# Cortex-A9
EXTRA_OECONF_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_zc702-zynq7 = " \
        ${GCC_CONFIGURE_A9} \
"

# Temporary hack to build gcc cross canadian for tclibc-newlib as --with-sysroot=/not/exist
# has been removed from TARGET_OS for elf and eabi in gcc-cross-canadian.inc

python() {
    extraoeconfgcc = d.getVar('EXTRA_OECONF')
    extraoeconfgcc += " --with-sysroot=/not/exist"
    d.delVar('EXTRA_OECONF')
    d.setVar('EXTRA_OECONF', extraoeconfgcc)
}
