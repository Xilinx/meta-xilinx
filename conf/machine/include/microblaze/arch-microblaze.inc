# MicroBlaze architecture tune feature configuration

# Architecture feature and override
TUNEVALID[microblaze] = "MicroBlaze"
MACHINEOVERRIDES =. "${@bb.utils.contains("TUNE_FEATURES", "microblaze", "microblaze:", "" ,d)}"

# Endian
TUNEVALID[bigendian] = "Use Microblaze Big Endian."

MBPKGARCH_ENDIAN = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", "eb", "el" ,d)}"

TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "bigendian", "-mbig-endian", "-mlittle-endian" ,d)}"

# General features
TUNEVALID[barrel-shift] = "Enable Hardware Barrel Shifter"
TUNEVALID[pattern-compare] = "Enable Pattern Compare Instructions"
TUNEVALID[reorder] = "Enable Reorder Instructions"

# General feature compiler args
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "barrel-shift", "-mxl-barrel-shift", "-mno-xl-barrel-shift" ,d)}"
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "pattern-compare", "-mxl-pattern-compare", "-mno-xl-pattern-compare" ,d)}"

# Disable reorder for v8.30 if pattern-compare is not enabled
TUNE_CCARGS += "${@bb.utils.contains("TUNE_FEATURES", "reorder", \
        bb.utils.contains("TUNE_FEATURES", "v8.30", \
            bb.utils.contains("TUNE_FEATURES", "pattern-compare", \
                "-mxl-reorder", "-mno-xl-reorder", d), \
            "-mxl-reorder", d), "-mno-xl-reorder", d)}"

# General feature package architecture formatting
MBPKGARCH_TUNE = ""
MBPKGARCH_TUNE .= "${@bb.utils.contains("TUNE_FEATURES", "barrel-shift", "-bs", "" ,d)}"
MBPKGARCH_TUNE .= "${@bb.utils.contains("TUNE_FEATURES", "pattern-compare", "-cmp", "" ,d)}"
MBPKGARCH_TUNE .= "${@bb.utils.contains("TUNE_FEATURES", "reorder", "-re", "" ,d)}"

# Additional features
require conf/machine/include/microblaze/feature-microblaze-versions.inc
require conf/machine/include/microblaze/feature-microblaze-math.inc

# Architecture name, either 'microblazeeb' or 'microblazeel' depending on endianess
TUNE_ARCH = "microblaze${@bb.utils.contains("TUNE_FEATURES", "bigendian", "eb", "el" ,d)}"

# Package Architecture formatting
TUNE_PKGARCH = "microblaze${MBPKGARCH_ENDIAN}${MBPKGARCH_VERSION}${MBPKGARCH_TUNE}${MBPKGARCH_MATH}"

