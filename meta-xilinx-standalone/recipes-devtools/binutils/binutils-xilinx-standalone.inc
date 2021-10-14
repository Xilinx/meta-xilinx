LDGOLD:xilinx-standalone = ""
LDGOLD_ALTS:xilinx-standalone = ""

EXTRA_OECONF:append:xilinx-standalone = " \
	--disable-gprof \
	--disable-shared \
	--enable-lto \
	--enable-static \
	--enable-multilib \
"

# CortexR5 and 32-bit arm are both "arm"
EXTRA_OECONF:append:xilinx-standalone:arm = " \
	--enable-interwork \
"

EXTRA_OECONF:append:xilinx-standalone:microblaze = " \
	--disable-initfini-array \
"
