include gcc-xilinx-standalone.inc

# Cortex-R5
EXTRA_OECONF_append_pn-gcc-cross-${TARGET_ARCH}_xilinx-standalone_cortexr5 = " \
        ${GCC_CONFIGURE_R5} \
"

# MB
EXTRA_OECONF_append_pn-gcc-cross-${TARGET_ARCH}_xilinx-standalone_zynqmp-pmu = " \
	${GCC_CONFIGURE_MB} \
"

# Cortex-A9
EXTRA_OECONF_append_pn-gcc-cross-${TARGET_ARCH}_xilinx-standalone_zc702-zynq7 = " \
        ${GCC_CONFIGURE_A9} \
"
