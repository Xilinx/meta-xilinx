# Configure fails on multilib when using a cache file, this re-sets it to nothing
EXTRA_OECONF_append = " --cache-file="

# Dont build libitm, etc.
RUNTIMETARGET_xilinx-standalone = "libstdc++-v3"

SYMVERS_CONF_xilinx-standalone =""


EXTRA_OECONF_append_xilinx-standalone = " --enable-multilib  --with-newlib"
EXTRA_OECONF_append_xilinx-standalone_cortexr5 = " --with-multilib-list=rmprofile"
EXTRA_OECONF_append_xilinx-standalone_zc702-zynq7 = " --with-multilib-list=aprofile"

# Recursve 5 levels due to the several combinations of multilibs built
FILES_libstdc++-staticdev += " \
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
