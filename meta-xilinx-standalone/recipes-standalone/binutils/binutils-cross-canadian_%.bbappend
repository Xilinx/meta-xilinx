include binutils-xilinx-standalone.inc


# The following is a copy of what is in OE-Core, without LDGOLD, because we have to do gold per machine instead
EXTRA_OECONF_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone = " \
	--program-prefix=${TARGET_PREFIX} \
	--disable-werror \
	--enable-deterministic-archives \ 
	--enable-plugins \
	${BINUTILS_CONFIGURE_COMMON_XILINX_STANDALONE} \
"


EXTRA_OECONF_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_cortexa53 = " \
        ${BINUTILS_CONFIGURE_A53} \
"

EXTRA_OECONF_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_cortexa72 = " \
        ${BINUTILS_CONFIGURE_A72} \
"
EXTRA_OECONF_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_cortexr5 = " \
        ${BINUTILS_CONFIGURE_R5} \
"
EXTRA_OECONF_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_zynqmp-pmu = " \
	${BINUTILS_CONFIGURE_MB} \
"
EXTRA_OECONF_pn-binutils-cross-canadian-${TARGET_ARCH}_xilinx-standalone_append_zc702-zynq7 = " \
        ${BINUTILS_CONFIGURE_A9} \
"
