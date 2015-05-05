DEFAULTTUNE ?= "microblaze"

require conf/machine/include/tune-microblaze-features.inc

TUNEVALID[microblaze] = "Microblaze"
AVAILTUNES += "microblaze"

TUNE_FEATURES_tune-microblaze = "microblaze"
PACKAGE_EXTRA_ARCHS_tune-microblaze = "${TUNE_PKGARCH}"

# override for architecture
MACHINEOVERRIDES =. "${@bb.utils.contains("TUNE_FEATURES", "microblaze", "microblaze:", "" ,d)}"

# Linux Configuration
KERNEL_IMAGETYPE ?= "linux.bin.ub"

# U-Boot Configuration
UBOOT_MACHINE ?= "microblaze-generic_config"

# linux-yocto KMACHINE
KMACHINE ?= "microblaze"

# default to use the gdb 7.7.1
PREFERRED_VERSION_gdb ?= "7.7.1"
PREFERRED_VERSION_gdb-cross ?= "7.7.1"
PREFERRED_VERSION_gdb-cross-canadian ?= "7.7.1"

