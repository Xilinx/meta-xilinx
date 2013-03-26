TARGET_FPU = "fpu-other"

TUNEVALID[fpu-soft] = "Use Microblaze software FPU"
TUNEVALID[fpu-hard] = "Enable Microblaze hardware FPU in basic mode."
TUNEVALID[fpu-hard-extended] = "Enable Microblaze hardware FPU in extended mode - conversion and square root instructions."

# Establish which fpu is configured in TUNE_FEATURES. Soft is the default for gcc.
MBCCARGSFPU_BASIC = "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard", "-mhard-float", "", d)}"
MBCCARGSFPU = "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard-extended", "-mhard-float -mxl-float-convert -mxl-float-sqrt", "${MBCCARGSFPU_BASIC}" ,d)}"

MBPKGFPU_BASIC = "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard", "-fb", "", d)}"
MBPKGFPU = "${@bb.utils.contains("TUNE_FEATURES", "fpu-hard-extended", "-fe", "${MBPKGFPU_BASIC}", d)}"

TUNECONFLICTS[fpu-hard] = "fpu-soft"
TUNECONFLICTS[fpu-hard-extended] = "fpu-soft"
