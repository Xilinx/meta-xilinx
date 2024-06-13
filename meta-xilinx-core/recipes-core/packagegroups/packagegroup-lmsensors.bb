DESCRIPTION = "PetaLinux packages that provides tools and drivers for monitoring temperatures, voltage"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

LMSENSORS_PACKAGES = " \
	lmsensors-sensors \
	lmsensors-libsensors \
	lmsensors-sensorsdetect \
	"

RDEPENDS:${PN} = "${LMSENSORS_PACKAGES}"
