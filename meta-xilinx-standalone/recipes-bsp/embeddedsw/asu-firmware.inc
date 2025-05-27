DESCRIPTION = "Application Security Unit"
SUMMARY = "Application Security Unit for Versal 2ve 2vm devices"

inherit xlnx-embeddedsw deploy

COMPATIBLE_HOST = "riscv32-xilinx-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# This is the default in most BSPs.  A MACHINE.conf can override this!
ASU_IMAGE_NAME ??= "asu-${MACHINE}"

inherit image-artifact-names

ASU_BASE_NAME ?= "${ASU_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "asu.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
