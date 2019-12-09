DISTRO_NAME = "Xilinx Standalone Distro"
DISTRO_VERSION = "1.0"
TARGET_VENDOR = "-xilinx"

# Make sure any users get the xilinx-standalone distro override
DISTROOVERRIDES_prepend = "${@'xilinx-standalone:' if d.getVar('DISTRO') != "xilinx-standalone" else ''}"

TCLIBC = "newlib"
TCLIBCAPPEND =""

# Change SDK name
SDK_VERSION = "xilinx-standalone"

require conf/distro/include/yocto-uninative.inc
INHERIT += "uninative"

# Hold this until it gets merged in core, we need libc.a and libgloss.a for cross-canadian
LIBC_DEPENDENCIES_append = " newlib-staticdev libgloss-staticdev"

ESW_CFLAGS ?= ""
