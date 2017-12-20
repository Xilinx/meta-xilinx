DEFAULTTUNE ?= "microblaze"

require conf/machine/include/microblaze/arch-microblaze.inc

AVAILTUNES += "microblaze"
TUNE_FEATURES_tune-microblaze = "microblaze"
PACKAGE_EXTRA_ARCHS_tune-microblaze = "${TUNE_PKGARCH}"

# U-Boot Configuration
UBOOT_MACHINE ?= "microblaze-generic_config"

