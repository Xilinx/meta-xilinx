include gcc-xilinx-standalone.inc

# Cortex-R5
EXTRA_OECONF_append_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_cortexr5 = " \
        ${GCC_CONFIGURE_R5} \
"

# MB
EXTRA_OECONF_append_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_zynqmp-pmu = " \
	${GCC_CONFIGURE_MB} \
"

# Cortex-A9
EXTRA_OECONF_append_pn-gcc-cross-canadian-${TARGET_ARCH}_xilinx-standalone_zc702-zynq7 = " \
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
