# Tune options for microblaze big endian

DEFAULTTUNE ?= "microblaze"
TUNE_ARCH = "microblaze"
TUNE_PKGARCH = "${TUNE_ARCH}"

AVAILTUNES += "microblaze"
TUNE_FEATURES_tune-microblaze ?= "fpu-soft"
PACKAGE_EXTRA_ARCHS_tune-microblaze = ""

# FPU Configuration
TUNEVALID[fpu-soft] = "Use software FPU."
TARGET_FPU = "${@bb.utils.contains("TUNE_FEATURES", "fpu-soft", "soft", "", d)}"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "fpu-soft", "-msoft-float", "", d)}"

# CPU Version Configuration
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "microblaze", "-mcpu=v8.30.a", "", d)}"

