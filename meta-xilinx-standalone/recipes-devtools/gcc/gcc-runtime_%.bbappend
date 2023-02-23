require gcc-configure-xilinx-standalone.inc

COMPATIBLE_HOST = "${HOST_SYS}"

EXTRA_OECONF:append:xilinx-standalone:class-target = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
"

EXTRA_OECONF:append:xilinx-standalone:aarch64:class-target = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

EXTRA_OECONF:append:xilinx-standalone:armv7r:class-target = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:xilinx-standalone:armv8r:class-target = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF:append:xilinx-standalone:microblaze:class-target = " \
	--without-long-double-128 \
	"

# Changes local to gcc-runtime...

# Dont build libitm, etc.
RUNTIMETARGET:xilinx-standalone:class-target = "libstdc++-v3"

do_install:append:xilinx-standalone:class-target() {
	# Fixup what gcc-runtime normally would do, we don't want linux directories!
	rm -rf ${D}${includedir}/c++/${BINV}/${TARGET_ARCH}${TARGET_VENDOR}-linux
}

FILES:${PN}-dbg:append:xilinx-standalone:class-target = "\
    ${libdir}/libstdc++.a-gdb.py \
"
