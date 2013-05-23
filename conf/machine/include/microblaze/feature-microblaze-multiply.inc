TUNEVALID[multiply-low] = "Enable Microblaze Hardware Multiply with low (32 bit) result."
TUNEVALID[multiply-high] = "Enable Microblaze Hardware Multiply with high (64 bit) result."

# Establish which Multiply output result size to use
MBCCARGSMUL_BASIC = "${@bb.utils.contains("TUNE_FEATURES", "multiply-low", "-mno-xl-soft-mul", "-mxl-soft-mul" ,d)}"
MBCCARGSMUL = "${@bb.utils.contains("TUNE_FEATURES", "multiply-high", "-mno-xl-soft-mul -mxl-multiply-high", "${MBCCARGSMUL_BASIC}" ,d)}"

MBPKGMUL_BASIC = "${@bb.utils.contains("TUNE_FEATURES", "multiply-low", "-ml", "" ,d)}"
MBPKGMUL = "${@bb.utils.contains("TUNE_FEATURES", "multiply-high", "-mh", "${MBPKGMUL_BASIC}" ,d)}"
