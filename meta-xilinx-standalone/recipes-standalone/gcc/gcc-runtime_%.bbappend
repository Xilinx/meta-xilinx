# Configure fails on multilib when using a cache file, this re-sets it to nothing
EXTRA_OECONF_append = " --cache-file="

# Dont build libitm, etc.
RUNTIMETARGET_xilinx-standalone = "libstdc++-v3"

# These overrides can potentially be cleaned (e.g. use just one) if it doesnt affect nativesdk class
EXTRA_OECONF_remove_xilinx-standalone_aarch64 = "--enable-symvers=gnu"
EXTRA_OECONF_remove_xilinx-standalone_arm = "--enable-symvers=gnu"

EXTRA_OECONF_append_xilinx-standalone_aarch64 = " --enable-multilib  --with-newlib"
EXTRA_OECONF_append_xilinx-standalone_cortexr5 = " --enable-multilib --with-newlib --with-multilib-list=rmprofile"

# Does cortexa9 needs multilib?

# Are the py files necessary?
FILES_libstdc++-staticdev += " \
    ${libdir}/libstdc++*.a* \
    ${libdir}/**/libsupc++.a* \
    ${libdir}/**/libstdc++*.a* \
    ${libdir}/**/libsupc++.a* \
    ${libdir}/**/**/**/libstdc++*.a* \
    ${libdir}/**/**/**/libsupc++.a* \
"
