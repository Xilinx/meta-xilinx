DESCRIPTION = "Platform Loader and Manager"
SUMMARY = "Platform Loader and Manager for Versal devices"

inherit xlnx-embeddedsw deploy

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# This is the default in most BSPs.  A MACHINE.conf can override this!
PLM_IMAGE_NAME ??= "plm-${MACHINE}"

inherit image-artifact-names

PLM_BASE_NAME ?= "${PLM_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "plm.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
