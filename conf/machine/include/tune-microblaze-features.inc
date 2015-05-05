# The order of these files defines the order of sections in the PKGARCH variable
require conf/machine/include/microblaze/feature-microblaze-endian.inc
require conf/machine/include/microblaze/feature-microblaze-versions.inc
require conf/machine/include/microblaze/feature-microblaze-barrel-shift.inc
require conf/machine/include/microblaze/feature-microblaze-multiply.inc
require conf/machine/include/microblaze/feature-microblaze-divide.inc
require conf/machine/include/microblaze/feature-microblaze-fpu.inc
require conf/machine/include/microblaze/feature-microblaze-pattern-compare.inc
require conf/machine/include/microblaze/feature-microblaze-reorder.inc

# Architecture name, either 'microblaze' or 'microblazeel' depending on endianess
TUNE_ARCH = "microblaze${@bb.utils.contains("TUNE_FEATURES", "little-endian", "el", "eb" ,d)}"

# Compiler args
TUNE_CCARGS += "${MBCCARGSENDIAN} ${MBCCARGSVERSION} ${MBCCARGSBARRELSHIFT} ${MBCCARGSMUL} ${MBCCARGSDIV} ${MBCCARGSFPU} ${MBCCARGSPATTERNCOMPARE} ${MBCCARGSREORDER}"

# Package Architecture formatting
MBPKGMATH = "${MBPKGMUL}${MBPKGDIV}${MBPKGFPU}"
TUNE_PKGARCH = "microblaze${MBPKGENDIAN}${MBPKGVERSION}${MBPKGBARRELSHIFT}${MBPKGSPATTERNCOMPARE}${MBPKGREORDER}${MBPKGMATH}"

