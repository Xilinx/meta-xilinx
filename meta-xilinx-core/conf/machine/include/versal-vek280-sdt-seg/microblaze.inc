# /cpus_microblaze@0/cpu@0
#   compatible = "pmc-microblaze";
AVAILTUNES += "microblaze-pmc"
TUNE_FEATURES:tune-microblaze-pmc = "microblaze v10.0 barrel-shift pattern-compare reorder multiply-high divide-hard fpu-soft"
PACKAGE_EXTRA_ARCHS:tune-microblaze-pmc = "${TUNE_PKGARCH}"

# /cpus_microblaze@1/cpu@0
#   compatible = "psm-microblaze";
AVAILTUNES += "microblaze-psm"
TUNE_FEATURES:tune-microblaze-psm = "microblaze v10.0 barrel-shift pattern-compare reorder multiply-high divide-hard fpu-soft"
PACKAGE_EXTRA_ARCHS:tune-microblaze-psm = "${TUNE_PKGARCH}"


require conf/machine/include/xilinx-microblaze.inc
