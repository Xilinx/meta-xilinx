include conf/machine/include/arm/armv7r/tune-cortexr5.inc

AVAILTUNES                            += "cortexr5hf"
ARMPKGARCH:tune-cortexr5hf             = "cortexr5"
TUNE_FEATURES:tune-cortexr5hf          = "${TUNE_FEATURES:tune-cortexr5} callconvention-hard"
PACKAGE_EXTRA_ARCHS:tune-cortexr5hf    = "cortexr5hf-vfpv3d16"
