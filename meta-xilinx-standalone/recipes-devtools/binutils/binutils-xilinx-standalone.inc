LDGOLD_xilinx-standalone = ""
LDGOLD_ALTS_xilinx-standalone = ""

EXTRA_OECONF:append_xilinx-standalone = " \
	--disable-gprof \
	--disable-shared \
	--enable-lto \
	--enable-static \
	--enable-multilib \
"

# CortexR5 and 32-bit arm are both "arm"
EXTRA_OECONF:append_xilinx-standalone:arm = " \
	--enable-interwork \
"

EXTRA_OECONF:append_xilinx-standalone:microblaze = " \
	--disable-initfini-array \
"
