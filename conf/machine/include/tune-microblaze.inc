# Tune options for microblaze
# The only tune 'feature' defined here is the microblaze tune.
# See the machine file (microblaze[el].conf) for details.

DEFAULTTUNE ?= "microblaze"

TUNE_ARCH = "microblazeel"

require conf/machine/include/tune-microblaze-features.inc

AVAILTUNES += "microblaze"
TUNEVALID[microblaze] = "Microblaze"
TUNE_FEATURES_tune-microblaze += "microblaze"

TUNE_CCARGS += "${MBCCARGSENDIAN} ${MBCCARGSVERSION} ${MBCCARGSBARRELSHIFT} ${MBCCARGSMUL} ${MBCCARGSDIV} ${MBCCARGSFPU} ${MBCCARGSPATTERNCOMPARE} ${MBCCARGSREORDER}"
TUNE_PKGARCH = "microblaze${MBPKGENDIAN}${MBPKGVERSION}${MBPKGBARRELSHIFT}${MBPKGMUL}${MBPKGDIV}${MBPKGFPU}${MBPKGSPATTERNCOMPARE}${MBPKGREORDER}"
PACKAGE_EXTRA_ARCHS_tune-microblaze = "${TUNE_PKGARCH}"
