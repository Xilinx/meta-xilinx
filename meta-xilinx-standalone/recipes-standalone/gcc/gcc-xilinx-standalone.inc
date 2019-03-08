# This inc file contains common configuration used for GCC for several
# MACHINES when using xilinx-standalone a DISTRO.
# This should be applicable to both cross and cross-canadian toolchain.


# By using tclibc-newlib we loose sysroot functionality due to some
# append/override behavior We need to get that back , the following append
# overrides everything on EXTRA_OECONF for gcc cross target it avoids
# overlapping with crt0 because of --enable-linker-id from EXTRA_OECONF

GCC_CONFIGURE_COMMON_XILINX_STANDALONE = " \
	--disable-libmudflap \
	--disable-libstdcxx-pch \
	--disable-nls \
	--enable-languages=${LANGUAGES} \
	--with-newlib \
"


GCC_CONFIGURE_A53 ?= " \
        --disable-bootstrap \
        --disable-multiarch \
        --disable-threads \
        --enable-c99 \
        --enable-checking=yes \
        --enable-fix-cortex-a53-835769 \
        --enable-fix-cortex-a53-843419 \
        --enable-linker-build-id \
        --enable-long-long \
        --enable-lto \
        --enable-plugins \
        --enable-shared \
        --with-arch=armv8-a \
        --with-cloog=no \
        --with-gnu-as \
        --with-gnu-ld \
        --with-isl=no \
        --with-ppl=no \
"



BINUTILS_CONFIGURE_A53 ?= " \
	--disable-gprof \
	--disable-libdecnumber \
	--disable-readline \
	--disable-shared \
	--enable-lto \
	--enable-static \
"
GCC_CONFIGURE_A72 ?= " \
        --disable-bootstrap \
        --disable-multiarch \
        --disable-threads \
        --enable-c99 \
        --enable-checking=yes \
        --enable-linker-build-id \
        --enable-long-long \
        --enable-lto \
	--enable-multilib \
        --enable-plugins \
        --enable-shared \
	--enable-threads=no \
        --with-cloog=no \
        --with-gnu-as \
        --with-gnu-ld \
        --with-isl=no \
	--with-ppl=no \
"
BINUTILS_CONFIGURE_A72 ?= " \
	--disable-gprof \
	--disable-libdecnumber \
	--disable-readline \
	--disable-shared \
	--enable-lto \
	--enable-static \
"



GCC_CONFIGURE_R5 ?= " \
        --disable-decimal-float \
        --disable-libffi \
        --disable-libgomp \
        --disable-libquadmath \
        --disable-libssp \
        --disable-shared \
        --disable-threads \
        --disable-tls \
        --enable-plugins \
        --with-gnu-as \
        --with-gnu-ld \
        --with-headers=yes \
        --with-host-libstdcxx=-static-libgcc \
         --with-multilib-list=rmprofile \
"
BINUTILS_CONFIGURE_R5 ?= " \
	--disable-docs \
	--disable-nls \
	--enable-interwork \
"


GCC_CONFIGURE_MB ?= " \
        --disable-__cxa_atexit \
        --disable-libgomp \
        --disable-shared \
        --enable-lto \
        --enable-target-optspace \
        --enable-threads=no \
        --with-isl=no \
        --with-ppl=no \
        --without-long-double-128 \
	--disable-multilib \
"
BINUTILS_CONFIGURE_MB ?= " \
	--disable-initfini-array \
	--disable-nls \
	--enable-gold=no \
	--enable-ld=yes \
	--enable-multilib \
"

GCC_CONFIGURE_A9 ?= " \
	--disable-bootstrap \
	--disable-multiarch \
	--disable-nls \
	--disable-threads \
	--enable-c99 \
	--enable-checking=yes \
	--enable-linker-build-id \
	--enable-long-long \
	--enable-lto \
	--enable-multilib \
	--enable-plugins \
	--enable-shared \
	--with-cloog=no \
	--with-gnu-as \
	--with-gnu-ld \
	--with-isl=no \
	--with-ppl=no \
"
BINUTILS_CONFIGURE_A9 ?= " \
	--disable-docs \
	--disable-nls \
	--enable-interwork \
"