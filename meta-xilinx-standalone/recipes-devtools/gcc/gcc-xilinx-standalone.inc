EXTRA_OECONF:append:xilinx-standalone = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
"

EXTRA_OECONF:append:xilinx-standalone:aarch64 = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

# Both arm and armv*r overrides are set w/ cortex r5
# So only set rmprofile if armv*r is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE:armv7r = "rmprofile"
ARM_PROFILE:armv8r = "rmprofile"
EXTRA_OECONF:append:xilinx-standalone:arm = " \
	--with-multilib-list=${ARM_PROFILE} \
	"

EXTRA_OECONF:append:xilinx-standalone:armv7r = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:xilinx-standalone:armv8r = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:xilinx-standalone:microblaze = " \
	--without-long-double-128 \
	"
