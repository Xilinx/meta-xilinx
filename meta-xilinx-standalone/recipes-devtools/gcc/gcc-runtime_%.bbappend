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
"

EXTRA_OECONF_append_xilinx-standalone_aarch64_class-target = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

# Both arm and cortexr5 overrides are set w/ r5
# So only set rmprofile if r5 is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE_cortexr5-zynqmp = "rmprofile"
ARM_PROFILE_cortexr5-versal = "rmprofile"

EXTRA_OECONF_append_xilinx-standalone_arm_class-target = " \
	--with-multilib-list=${ARM_PROFILE} \
	"

EXTRA_OECONF_append_xilinx-standalone_cortexr5-versal_class-target = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF_append_xilinx-standalone_cortexr5-versal_class-target = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF_append_xilinx-standalone_microblaze_class-target = " \
	--disable-__cxa_atexit \
	--enable-target-optspace \
	--without-long-double-128 \
	"

# Changes local to gcc-runtime...

# Dont build libitm, etc.
RUNTIMETARGET_xilinx-standalone_class-target = "libstdc++-v3"

do_install_append_xilinx-standalone_class-target() {
	# Fixup what gcc-runtime normally would do, we don't want linux directories!
	rm -rf ${D}${includedir}/c++/${BINV}/${TARGET_ARCH}${TARGET_VENDOR}-linux

	# The multilibs have different headers, so stop combining them!
	if [ "${TARGET_VENDOR_MULTILIB_ORIGINAL}" != "" -a "${TARGET_VENDOR}" != "${TARGET_VENDOR_MULTILIB_ORIGINAL}" ]; then
		rm -rf ${D}${includedir}/c++/${BINV}/${TARGET_ARCH}${TARGET_VENDOR_MULTILIB_ORIGINAL}-${TARGET_OS}
	fi
}

FILES_${PN}-dbg_append_xilinx-standalone_class-target = "\
    ${libdir}/libstdc++.a-gdb.py \
"
