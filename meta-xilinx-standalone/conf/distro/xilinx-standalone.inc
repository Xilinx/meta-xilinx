DISTRO_NAME = "Xilinx Standalone Distro"
DISTRO_VERSION = "1.0"
TARGET_VENDOR = "-xilinx"

# Make sure any users get the xilinx-standalone distro override
DISTROOVERRIDES:prepend = "${@'xilinx-standalone:' if d.getVar('DISTRO') != "xilinx-standalone" else ''}"

TCLIBC = "newlib"
TCLIBCAPPEND =""

# Change SDK name
SDK_VERSION = "xilinx-standalone"

# Hold this until it gets merged in core, we need libc.a and libgloss.a for cross-canadian
LIBC_DEPENDENCIES:append = " newlib-staticdev libgloss-staticdev"

ESW_CFLAGS ?= ""

INHERIT += "buildhistory"

# Cortex R5 requires an additional cflag to be passed for compatibility with the embeddedsw
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'cortexr5', ' -DARMR5', '', d)}"

# Make sure all regular recipes are excluded from compatibility
# Avoid using this for native, nativesdk or cross recipes
COMPATOS = ""
# Set the regex for target recipes only, follow TARGET_OS default
COMPATOS:class-target = ".*-linux${LIBCEXTENSION}${ABIEXTENSION}"
COMPATIBLE_HOST ?= "${COMPATOS}"

# Clear defaults
DISTRO_FEATURES_BACKFILL:xilinx-standalone = ""
VIRTUAL-RUNTIME_init_manager:xilinx-standalone = ""

PREFERRED_PROVIDER_virtual/kernel = "linux-dummy"

PREFERRED_VERSION_libmetal = "${@'standalone' if 'xilinx-standalone-exp' in d.getVar('BBFILE_COLLECTIONS').split() else ''}"
PREFERRED_VERSION_open-amp = "${@'standalone' if 'xilinx-standalone-exp' in d.getVar('BBFILE_COLLECTIONS').split() else ''}"

# Hold this until it gets merged in core, we need libc.a and libgloss.a for cross-canadian
LIBC_DEPENDENCIES:append = " newlib-staticdev libgloss-staticdev"

# No cached configsite files
TOOLCHAIN_NEED_CONFIGSITE_CACHE = ""

# Workaround for pulling in nativesdk-mingw-w64-winpthreads
TOOLCHAIN_HOST_TASK:append:sdkmingw32 = " nativesdk-mingw-w64-winpthreads"
