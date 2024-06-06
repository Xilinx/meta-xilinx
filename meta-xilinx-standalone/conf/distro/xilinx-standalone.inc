DISTRO_NAME = "Xilinx Standalone Distro"
DISTRO_VERSION = "1.0"
TARGET_VENDOR = "-xilinx"

TCLIBC = "newlib"
TCLIBCAPPEND =""

# Change SDK name
SDK_VERSION = "xilinx-standalone"

# Hold this until it gets merged in core, we need libc.a and libgloss.a for cross-canadian
LIBC_DEPENDENCIES:append:xilinx-standalone = " newlib-staticdev libgloss-staticdev"

ESW_CFLAGS ?= ""

# Cortex R5 requires an additional cflag to be passed for compatibility with the embeddedsw
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'cortexr5', ' -DARMR5', '', d)}"
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'cortexr52', ' -DARMR5 -DARMR52', '', d)}"
TUNE_CCARGS:append:versal = " -Dversal"
TUNE_CCARGS:append:versal-net = " -DVERSAL_NET"

# Make sure all regular recipes are excluded from compatibility
# Avoid using this for native, nativesdk or cross recipes
COMPATOS = ""
# Set the regex for target recipes only, follow TARGET_OS default
COMPATOS:class-target = ".*-linux${LIBCEXTENSION}${ABIEXTENSION}"
COMPATIBLE_HOST ?= "${COMPATOS}"

# Clear defaults
PREFERRED_PROVIDER_virtual/kernel = "linux-dummy"
PREFERRED_PROVIDER_libmetal = "libmetal-xlnx"
PREFERRED_PROVIDER_open-amp = "open-amp-xlnx"

# Workaround for pulling in nativesdk-mingw-w64-winpthreads
TOOLCHAIN_HOST_TASK:append:sdkmingw32 = " nativesdk-mingw-w64-winpthreads"

# Disable static for all classes, except for target.  This will ensure we can
# share more recipe hashes with other distros, while still building
# static binaries for baremetal targets (by default).  The user can of course
# still override this, but will need to specifically select class-target to
# do so.
DISABLE_STATIC:class-target ?= ""

require conf/distro/include/no-static-libs.inc
require conf/distro/include/yocto-uninative.inc
INHERIT += "uninative"

BB_SIGNATURE_HANDLER ?= "OEEquivHash"
BB_HASHSERVE ??= "auto"

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc qemu-usermode"
