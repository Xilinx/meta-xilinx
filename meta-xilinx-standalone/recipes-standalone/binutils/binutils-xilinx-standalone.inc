# This inc file contains common configuration used for Binutils for several
# MACHINES when using xilinx-standalone a DISTRO.
# This should be applicable to both cross and cross-canadian toolchain.

BINUTILS_CONFIGURE_R5 ?= " \
	--disable-docs \
	--disable-nls \
	--enable-interwork \
"

BINUTILS_CONFIGURE_MB ?= " \
	--disable-initfini-array \
	--disable-nls \
	--enable-gold=no \
	--enable-ld=yes \
	--enable-multilib \
"

BINUTILS_CONFIGURE_A9 ?= " \
	--disable-docs \
	--disable-nls \
	--enable-interwork \
"
