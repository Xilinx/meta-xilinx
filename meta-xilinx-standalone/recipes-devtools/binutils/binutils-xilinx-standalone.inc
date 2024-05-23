LDGOLD:xilinx-standalone = ""
LDGOLD_ALTS:xilinx-standalone = ""

EXTRA_OECONF:append:xilinx-standalone = " \
	--disable-gprof \
	--disable-shared \
	--enable-lto \
	--enable-static \
"

# CortexR5 and 32-bit arm are both "arm"
EXTRA_OECONF:append:arm:xilinx-standalone = " \
	--enable-interwork \
"

EXTRA_OECONF:append:microblaze:xilinx-standalone = " \
	--disable-initfini-array \
"

# Workaround for:
# ERROR: binutils-cross-canadian-aarch64-2.39-r0 do_package_qa: QA
# Issue: non -staticdev package contains static .a library:
# binutils-cross-canadian-aarch64 path
# '/usr/local/oe-sdk-hardcoded-buildpath/sysroots/x86_64-oesdk-linux/usr/lib/aarch64-xilinx-elf/bfd-plugins/libdep.a'
# [staticdev]
INSANE_SKIP:${PN} += "staticdev"
