# Copy of gcc-xilinx-standalone.inc, but with _class-target added
LINKER_HASH_STYLE_xilinx-standalone_class-target = ""
SYMVERS_CONF_xilinx-standalone_class-target = ""

EXTRA_OECONF_append_xilinx-standalone_class-target = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
	--enable-multilib \
"

EXTRA_OECONF_append_xilinx-standalone_aarch64_class-target = " \
	--disable-multiarch \
	--enable-fix-cortex-a53-835769 \
	--enable-fix-cortex-a53-843419 \
	--with-arch=armv8-a \
	"

# Both arm and cortexr5 overrides are set w/ r5
# So only set rmprofile if r5 is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE_cortexr5 = "rmprofile"
EXTRA_OECONF_append_xilinx-standalone_arm_class-target = " \
	--with-multilib-list=${ARM_PROFILE} \
	"

EXTRA_OECONF_append_xilinx-standalone_cortexr5_class-target = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF_append_xilinx-standalone_microblaze_class-target = " \
	--disable-__cxa_atexit \
	--enable-target-optspace \
	--without-long-double-128 \
	"

# Changes local to gcc-runtime...

# Due to multilibs, we need to clear the default TUNE_CCARGS on arm
TUNE_CCARGS_xilinx-standalone_arm_class-target = ""

# Configure fails on multilib when using a cache file, this re-sets it to nothing
EXTRA_OECONF_append_xilinx-standalone_class-target = " --cache-file="

# Dont build libitm, etc.
RUNTIMETARGET_xilinx-standalone_class-target = "libstdc++-v3"

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

