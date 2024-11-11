EXTRA_OECONF:append:xilinx-standalone = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
	--disable-tm-clone-registry \
"

EXTRA_OECONF:append:aarch64:xilinx-standalone = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

EXTRA_OECONF:append:armv7r:xilinx-standalone = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:armv8r:xilinx-standalone = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:microblaze:xilinx-standalone = " \
	--without-long-double-128 \
	"
