# When building multiple, we need to depend on the multilib newlib
DEPENDS:append:xilinx-standalone:baremetal-multilib-tc = " ${MLPREFIX}newlib"

# RISC-V mulitlib compiler expects the newlib to be in the 'libdir', but places it in ${D}/usr/lib
do_install:append:xilinx-standalone:baremetal-multilib-tc () {
    if [ "/usr/lib" != "${libdir}" ]; then
        for each in ${D}/usr/lib/* ; do
            if [ -f ${each} ]; then
                mv -v ${each} ${D}/${libdir}
            fi
        done
    fi
}
