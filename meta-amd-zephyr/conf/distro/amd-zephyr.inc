DISTRO_NAME = "AMD Zephyr RTOS distribution"
DISTRO_VERSION = "1.0"
TARGET_VENDOR = "-amd"

# Zephyr uses newlib for kernel, drivers and applications.
TCLIBC ?= "newlib"

TEST_TARGET = "QemuTargetZephyr"
TEST_SUITES = "zephyr"

INHERIT += "siteinfo-zephyr"

# TODO - Inheriting zephyr-qemuboot bbclass results in parse errors with
# multiconfig builds. Hence disble it for now.
# ZEPHYR_INHERIT_CLASSES += "zephyr-qemuboot"

BB_SIGNATURE_HANDLER ?= "OEEquivHash"
BB_HASHSERVE ??= "auto"

# Use Zephyr SDK toolchain to build application for cortex-r and microblaze-v
# processor.
ZEPHYR_TOOLCHAIN_VARIANT = "zephyr"

# Deploy SDT artifacts alongside Zephyr applications so the
# generated system device tree and related boot artifacts are
# available in the deploy directory. Only do this for Zephyr
# machines (ZEPHYR_BOARD is set) that actually define SDT_URI
# (i.e. SDT-based machines); generic or upstream Zephyr machines
# such as xenvm do not have SDT artifacts to deploy.
do_deploy[depends] += "${@'sdt-artifacts:do_deploy' if d.getVar('ZEPHYR_BOARD') and d.getVar('SDT_URI') else ''}"
