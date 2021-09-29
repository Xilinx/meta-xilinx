DEFAULTTUNE ?= "cortexr5"

require conf/machine/include/arm/arch-armv8a.inc

TUNEVALID[armrm] = "Enable ARM Cortex-R/M Family"
MACHINEOVERRIDES =. "${@bb.utils.contains('TUNE_FEATURES', 'armrm', 'armrm:', '' ,d)}"

TUNEVALID[cortexr5] = "Enable Cortex-r5 specific processor optimizations"
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'cortexr5', ' -mcpu=cortex-r5', '', d)}"

AVAILTUNES += "cortexr5"
ARMPKGARCH:tune-cortexr5 = "cortexr5"

TUNE_FEATURES:tune-cortexr5 = "armrm cortexr5"
PACKAGE_EXTRA_ARCHS:tune-cortexr5 = "${TUNE_PKGARCH}"

AVAILTUNES += "cortexr5f"
ARMPKGARCH:tune-cortexr5f = "cortexr5f"

TUNE_FEATURES:tune-cortexr5f = "armrm cortexr5 vfpv3d16 callconvention-hard"
PACKAGE_EXTRA_ARCHS:tune-cortexr5f = "${TUNE_PKGARCH}"
