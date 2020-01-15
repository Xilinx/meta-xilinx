# This inc file contains common configuration used for GCC for several
# MACHINES when using xilinx-standalone a DISTRO.
# This should be applicable to both cross and cross-canadian toolchain.

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
	--with-multilib-list=aprofile \
"
