BINUTILS_CONFIGURE_COMMON_XILINX_STANDALONE = " \
	--disable-gdb \
	--disable-sim \
"


# The following is a copy of what is in OE-Core, without LDGOLD, because we have to do gold per machine instead
EXTRA_OECONF_pn-binutils-cross-${TARGET_ARCH}_xilinx-standalone = " \
	--program-prefix=${TARGET_PREFIX} \
	--disable-werror \
	--enable-deterministic-archives \ 
	--enable-plugins \
	${BINUTILS_CONFIGURE_COMMON_XILINX_STANDALONE} \
"
