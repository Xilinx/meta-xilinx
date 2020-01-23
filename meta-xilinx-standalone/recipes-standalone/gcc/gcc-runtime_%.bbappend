# Configure fails on multilib when using a cache file, this re-sets it to nothing
EXTRA_OECONF_append_xilinx-standalone_class-target = " --cache-file="

# Dont build libitm, etc.
RUNTIMETARGET_xilinx-standalone_class-target = "libstdc++-v3"

SYMVERS_CONF_xilinx-standalone_class-target =""


EXTRA_OECONF_append_xilinx-standalone_class-target = " --enable-multilib  --with-newlib"
EXTRA_OECONF_append_xilinx-standalone_cortexr5_class-target = " --with-multilib-list=rmprofile"
EXTRA_OECONF_append_xilinx-standalone_zc702-zynq7_class-target = " --with-multilib-list=aprofile"

# Recursve 5 levels due to the several combinations of multilibs built
FILES_libstdc++-staticdev_append_xilinx-standalone_class-target = " \
    ${libdir}/libsupc++.a* \
    ${libdir}/libstdc++*.a* \
    ${libdir}/**/libsupc++.a* \
    ${libdir}/**/libstdc++*.a* \
    ${libdir}/**/**/libsupc++.a* \
    ${libdir}/**/**/libstdc++*.a* \
    ${libdir}/**/**/**/libsupc++.a* \
    ${libdir}/**/**/**/libstdc++*.a* \
    ${libdir}/**/**/**/**/libsupc++.a* \
    ${libdir}/**/**/**/**/libstdc++*.a* \
    ${libdir}/**/**/**/**/**/libsupc++.a* \
    ${libdir}/**/**/**/**/**/libstdc++*.a* \
"

