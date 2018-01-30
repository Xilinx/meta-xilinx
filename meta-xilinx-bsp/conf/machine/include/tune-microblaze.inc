DEFAULTTUNE ?= "microblaze"

require conf/machine/include/microblaze/arch-microblaze.inc

AVAILTUNES += "microblaze"
TUNE_FEATURES_tune-microblaze = "microblaze"
PACKAGE_EXTRA_ARCHS_tune-microblaze = "${TUNE_PKGARCH}"

# Linux Configuration
KERNEL_IMAGETYPE ?= "linux.bin.ub"
KERNEL_IMAGETYPES += "simpleImage.mb"

# U-Boot Configuration
UBOOT_MACHINE ?= "microblaze-generic_config"

# default to use the gdb 7.7.1
PREFERRED_VERSION_gdb ?= "7.7.1"
PREFERRED_VERSION_gdb-cross ?= "7.7.1"
PREFERRED_VERSION_gdb-cross-canadian ?= "7.7.1"

