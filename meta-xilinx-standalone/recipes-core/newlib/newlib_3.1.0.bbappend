
EXTRA_OECONF_append_xilinx-standalone = " \
	--enable-newlib-io-c99-formats \
	--enable-newlib-io-long-long \
	--enable-newlib-io-float \
	--enable-newlib-io-long-double \
	--disable-newlib-supplied-syscalls \
"

# Avoid trimmping CCARGS from CC by newlib configure
do_configure_prepend(){
    export CC_FOR_TARGET="${CC}"
}

# Fix for multilib newlib installations
do_install_prepend() {
        mkdir -p $(dirname ${D}${libdir})
        mkdir -p $(dirname ${D}${includedir})
}
