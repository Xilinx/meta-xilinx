# Some of the currently required multilibs require specific workarounds.
# The override values below are linked specifically to the baremetal toolchain BSPs

do_configure_prepend(){
    export CC_FOR_TARGET="${CC}"
}

# Fix for multilib newlib installations
do_install_prepend() {
	mkdir -p $(dirname ${D}${libdir})
	mkdir -p $(dirname ${D}${includedir})
}
