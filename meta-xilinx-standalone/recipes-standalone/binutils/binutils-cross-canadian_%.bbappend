include binutils-xilinx-standalone.inc

EXTRA_OECONF_append_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_cortexr5 = " \
        ${BINUTILS_CONFIGURE_R5} \
"
EXTRA_OECONF_append_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_zynqmp-pmu = " \
	${BINUTILS_CONFIGURE_MB} \
"
EXTRA_OECONF_append_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_zc702-zynq7 = " \
        ${BINUTILS_CONFIGURE_A9} \
"
