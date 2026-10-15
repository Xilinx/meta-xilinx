SUMMARY = "lm_sensors hardware-monitoring tools and drivers."
DESCRIPTION = "PetaLinux packages that provides tools and drivers for \
monitoring temperatures, voltage"

# Due to lmsensor bbappend switch it to MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LMSENSORS_PACKAGES = " \
	lmsensors-sensors \
	lmsensors-libsensors \
	lmsensors-sensorsdetect \
	"

RDEPENDS:${PN} = "${LMSENSORS_PACKAGES}"
