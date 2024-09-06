require gcc-configure-xilinx-standalone.inc

COMPATIBLE_HOST:xilinx-standalone = "${HOST_SYS}"

EXTRA_OECONF:append:class-target:xilinx-standalone = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
	--disable-tm-clone-registry \
"

EXTRA_OECONF:append:aarch64:class-target:xilinx-standalone = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

EXTRA_OECONF:append:armv7r:class-target:xilinx-standalone = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:armv8r:class-target:xilinx-standalone = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:microblaze:class-target:xilinx-standalone = " \
	--without-long-double-128 \
	"

# Changes local to gcc-runtime...

# Dont build libitm, etc.
RUNTIMETARGET:class-target:xilinx-standalone = "libstdc++-v3"

do_install:append:class-target:xilinx-standalone() {
	# Fixup what gcc-runtime normally would do, we don't want linux directories!
	rm -rf ${D}${includedir}/c++/${BINV}/${TARGET_ARCH}${TARGET_VENDOR}-linux
}

FILES:${PN}-dbg:append:class-target:xilinx-standalone = "\
    ${libdir}/libstdc++.a-gdb.py \
"
