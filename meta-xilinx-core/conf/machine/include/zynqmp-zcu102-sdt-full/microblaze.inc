# /cpus_microblaze@0/cpu@0
#   compatible = "pmu-microblaze";
AVAILTUNES += "microblaze-pmu"
TUNE_FEATURES:tune-microblaze-pmu = "microblaze v9.2 barrel-shift pattern-compare reorder fpu-soft"
PACKAGE_EXTRA_ARCHS:tune-microblaze-pmu = "${TUNE_PKGARCH}"


require conf/machine/include/xilinx-microblaze.inc
