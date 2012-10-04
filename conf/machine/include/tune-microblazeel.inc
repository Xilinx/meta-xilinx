# Tune options for microblaze endian little
DEFAULTTUNE ?= "microblazeel"

#TUNEVALID[m32] = "Microblaze ELF32 standard ABI"
#TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "m32", "", "", d)}"
TUNE_ARCH = "microblazeel"
#"${@bb.utils.contains("TUNE_FEATURES", "m32", "microblazeel", "", d)}"

TUNEVALID[fpu-soft] = "Use software FPU."
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "fpu-soft", "-msoft-float", "", d)}"
TARGET_FPU .= "${@bb.utils.contains("TUNE_FEATURES", "fpu-soft", "soft", "", d)}"

TUNE_PKGARCH = "${TUNE_ARCH}"

AVAILTUNES += "microblazeel"
TUNE_FEATURES_tune-microblazeel ?= "fpu-soft"
PACKAGE_EXTRA_ARCHS_tune-microblazeel = ""

TARGET_FPU = "soft"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "microblazeel", "-mcpu=v8.30.a", "", d)}"

#MACHINEOVERRIDES .= "microblaze"
