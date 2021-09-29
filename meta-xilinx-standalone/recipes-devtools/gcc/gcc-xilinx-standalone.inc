EXTRA_OECONF:append_xilinx-standalone = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
"

EXTRA_OECONF:append_xilinx-standalone:aarch64 = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

# Both arm and armrm overrides are set w/ cortex r5
# So only set rmprofile if armrm is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE:armrm = "rmprofile"
EXTRA_OECONF:append_xilinx-standalone:arm = " \
	--with-multilib-list=${ARM_PROFILE} \
	"

EXTRA_OECONF:append_xilinx-standalone:armrm = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append_xilinx-standalone:microblaze = " \
	--enable-target-optspace \
	--without-long-double-128 \
	"
