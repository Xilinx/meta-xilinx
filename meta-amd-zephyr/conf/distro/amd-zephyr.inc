DISTRO_NAME = "AMD Zephyr Distro"
DISTRO_VERSION = "1.0"
TARGET_VENDOR = "-amd"

# Zephyr uses newlib for kernel, drivers and applications.
TCLIBC ?= "newlib"
TCLIBCAPPEND =""

TEST_TARGET = "QemuTargetZephyr"
TEST_SUITES = "zephyr"

INHERIT += "siteinfo-zephyr"

# TODO - Inheriting zephyr-qemuboot bbclass results in parse errors with
# multiconfig builds. Hence disble it for now.
# ZEPHYR_INHERIT_CLASSES += "zephyr-qemuboot"

require conf/distro/include/yocto-uninative.inc
INHERIT += "uninative"

BB_SIGNATURE_HANDLER ?= "OEEquivHash"
BB_HASHSERVE ??= "auto"

# Use Zephyr SDK toolchain to build application for cortex-r and microblaze-v
# processor.
ZEPHYR_TOOLCHAIN_VARIANT = "zephyr"
