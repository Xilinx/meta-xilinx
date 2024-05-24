DESCRIPTION = "PMU Firmware"

inherit xlnx-embeddedsw deploy

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:zynqmp = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# This is the default in most BSPs.  A MACHINE.conf can override this!
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-${MACHINE}"

inherit image-artifact-names

PMU_FIRMWARE_BASE_NAME ?= "${PMU_FIRMWARE_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "executable.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
