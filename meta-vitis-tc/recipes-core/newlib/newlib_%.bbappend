# Fix for multilib newlib installations
do_install:prepend:xilinx-standalone:baremetal-multilib-tc () {
        mkdir -p $(dirname ${D}${libdir})
        mkdir -p $(dirname ${D}${includedir})
}

