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

# Prevent warnings about 'buildpaths'
# After investigation it appears a debuginfo directory path to the build
# directory is being inserted into the objects.  This path is not actually
# being referred to by any components.  Why this is happening is unknown,
# so we avoid the warning as we know this is not an actual issue.
WARN_QA:remove = "buildpaths"

# By default PetaLinux uses the own-mirror class to access cached version of
# the software.  While this distribution configuration is not PetaLinux, nor
# should it require PetaLinux to operating properly, including the same mirror
# configuration will unify the Linux and non-Linux (baremetal) configurations.
# Do this via include so if meta-petalinux is not part of the build this will
# not error.  This also allows the user to choose to handle the download
# mirroring in their own way for non-PetaLinux like configs.
include conf/distro/include/petalinux-mirrors.conf
