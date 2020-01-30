# Any changes to this file should be synced with gcc-runtime_%.bbappend

LINKER_HASH_STYLE_xilinx-standalone = ""
SYMVERS_CONF_xilinx-standalone = ""

EXTRA_OECONF_append_xilinx-standalone = " \
	--disable-libstdcxx-pch \
	--with-newlib \
	--disable-threads \
	--enable-plugins \
	--with-gnu-as \
	--disable-libitm \
	--enable-multilib \
"

EXTRA_OECONF_append_xilinx-standalone_aarch64 = " \
	--disable-multiarch \
	--with-arch=armv8-a \
	"

# Both arm and cortexr5 overrides are set w/ r5
# So only set rmprofile if r5 is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE_cortexr5-zynqmp = "rmprofile"
ARM_PROFILE_cortexr5-versal = "rmprofile"
EXTRA_OECONF_append_xilinx-standalone_arm = " \
	--with-multilib-list=${ARM_PROFILE} \
	"

EXTRA_OECONF_append_xilinx-standalone_cortexr5-zynqmp = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF_append_xilinx-standalone_cortexr5-versal = " \
	--disable-tls \
	--disable-decimal-float \
	"

EXTRA_OECONF_append_xilinx-standalone_microblaze = " \
	--disable-__cxa_atexit \
	--enable-target-optspace \
	--without-long-double-128 \
	"
