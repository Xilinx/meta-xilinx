DESCRIPTION = "First Stage Bootloader"

inherit xlnx-embeddedsw deploy

COMPATIBLE_HOST = ".*-(elf|.*eabi)"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# This is the default in most BSPs.  A MACHINE.conf can override this!
FSBL_IMAGE_NAME ??= "fsbl-${MACHINE}"

inherit image-artifact-names

FSBL_BASE_NAME ?= "${FSBL_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "fsbl.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
