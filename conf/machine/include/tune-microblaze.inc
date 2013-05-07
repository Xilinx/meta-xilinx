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
MBPKGMATH = "${MBPKGMUL}${MBPKGDIV}${MBPKGFPU}"
TUNE_PKGARCH = "microblaze${MBPKGENDIAN}${MBPKGVERSION}${MBPKGBARRELSHIFT}${MBPKGSPATTERNCOMPARE}${MBPKGREORDER}${MBPKGMATH}"
PACKAGE_EXTRA_ARCHS_tune-microblaze = "${TUNE_PKGARCH}"

# Linux Configuration
PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_VERSION_linux-xlnx ?= "3.8%"

KERNEL_IMAGETYPE ?= "linux.bin"
IMAGE_FSTYPES ?= "cpio ext2 ext2.gz.u-boot ext2.gz"
IMAGE_CLASSES += "image_types_uboot"

PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx"
PREFERRED_VERSION_u-boot-xlnx ?= "v2013.01%"
UBOOT_MACHINE ?= "microblaze-generic"
