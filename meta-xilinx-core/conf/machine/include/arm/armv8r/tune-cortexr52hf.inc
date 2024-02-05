require conf/machine/include/arm/armv8r/tune-cortexr52.inc

# simd is special, we don't pass this to the -mfpu, it's implied
TUNE_CCARGS_MFLOAT = "${@ bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hard', 'softfp', d) if (d.getVar('TUNE_CCARGS_MFPU') != '' or bb.utils.contains('TUNE_FEATURES', 'simd', True, False, d)) else '' }"

AVAILTUNES                             += "cortexr52hf"
ARMPKGARCH:tune-cortexr52hf             = "cortexr52"
TUNE_FEATURES:tune-cortexr52hf          = "${TUNE_FEATURES:tune-cortexr52} callconvention-hard"
PACKAGE_EXTRA_ARCHS:tune-cortexr52hf    = "cortexr52hf"
