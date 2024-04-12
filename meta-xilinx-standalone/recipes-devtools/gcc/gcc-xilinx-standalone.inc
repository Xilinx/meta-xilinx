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
	--disable-tm-clone-registry \
	"
